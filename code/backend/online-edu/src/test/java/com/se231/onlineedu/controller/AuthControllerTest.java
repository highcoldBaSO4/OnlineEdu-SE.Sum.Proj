package com.se231.onlineedu.controller;

import com.se231.onlineedu.jwtTest.UserDetailsDummy;
import com.se231.onlineedu.message.request.SignUpForm;
import com.se231.onlineedu.message.response.JwtResponse;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.WebSecurityConfig;
import com.se231.onlineedu.security.jwt.JwtAuthEntryPoint;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.security.services.UserDetailsServiceImpl;
import com.se231.onlineedu.service.AuthService;
import com.se231.onlineedu.service.UserService;
import io.jsonwebtoken.Clock;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author yuxuanLiu
 * @date 2019/07/31
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@Import(WebSecurityConfig.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @TestConfiguration
    static class asdff {
        @Bean
        public JwtProvider jwtProvider() {
            return new JwtProvider();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;


    @MockBean
    AuthService authService;

    @MockBean
    UserService userService;

    @Mock
    private Clock clockMocker;

    @Autowired
    private JwtProvider jwtProvider;

    private String token;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(jwtProvider, "jwtExpiration", 3600000); // an hour
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret", "mySecret");

        when(clockMocker.now())
                .thenReturn(DateUtil.now());

        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setRoles(List.of(new Role(RoleType.ROLE_TEACHING_ADMIN), new Role(RoleType.ROLE_ADMIN), new Role(RoleType.ROLE_USER)));
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(1L)).thenReturn(userOptional);
        when(userRepository.findByUsername("john")).thenReturn(userOptional);

        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(2L);
        Course course3 = new Course();
        course3.setId(3L);
        Notice notice = new Notice();
        notice.setContent("content");
        notice.setTitle("title");
        course1.setNotices(List.of(notice));

        Learn learn = new Learn();
        LearnPrimaryKey learnPrimaryKey = new LearnPrimaryKey();
        learnPrimaryKey.setCourse(course3);
        learnPrimaryKey.setStudent(user);
        learn.setLearnPrimaryKey(learnPrimaryKey);
        user.setLearns(List.of(learn));

        token = getToken("john");
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(jwtProvider.generateJwtToken(new UserDetailsDummy("john")));
        when(authService.userSignIn(anyString(),anyString())).thenReturn(jwtResponse);
        when(authService.userSignUp(any(SignUpForm.class),any(HttpSession.class))).thenReturn(user);
        when(authService.addTeachingAdmin(1L)).thenReturn("ok");
        when(authService.saveRegisteredUser(any(User.class))).thenReturn(user);
    }

    private String getToken(String name) {
        return "Bearer " + jwtProvider.generateJwtToken(new UserDetailsDummy(name));
    }

    @Test
    public void login() throws Exception {
        mvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"liu123\",\"password\":\"liu123\"}\n"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    public void signUp() throws Exception {
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"liu123\",\"password\":\"liu123\"}\n"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void addAdmin() throws Exception {
        mvc.perform(post("/api/auth/1/teachingAdmin/").header("Authorization", token).param("id","1"))
                .andExpect(content().string("ok"));
    }


    @Test
    public void registrationConfirm() throws Exception {
        VerificationToken verificationToken = new VerificationToken("111111");
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setRoles(List.of(new Role(RoleType.ROLE_TEACHING_ADMIN), new Role(RoleType.ROLE_ADMIN), new Role(RoleType.ROLE_USER)));

        mvc.perform(get("/api/auth/registrationConfirm").sessionAttr("token",verificationToken).sessionAttr("user",user).param("verificationToken","111111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void reregistrationConfirmWrong() throws Exception {
        VerificationToken verificationToken = new VerificationToken("111121");
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setRoles(List.of(new Role(RoleType.ROLE_TEACHING_ADMIN), new Role(RoleType.ROLE_ADMIN), new Role(RoleType.ROLE_USER)));

        mvc.perform(get("/api/auth/registrationConfirm").sessionAttr("token",verificationToken).sessionAttr("user",user).param("verificationToken","111111"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reregistrationConfirmExpired() throws Exception {
        VerificationToken verificationToken = new VerificationToken("111111", DateUtil.yesterday());
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setRoles(List.of(new Role(RoleType.ROLE_TEACHING_ADMIN), new Role(RoleType.ROLE_ADMIN), new Role(RoleType.ROLE_USER)));

        mvc.perform(get("/api/auth/registrationConfirm").sessionAttr("token",verificationToken).sessionAttr("user",user).param("verificationToken","111111"))
                .andExpect(status().isBadRequest());
    }
}