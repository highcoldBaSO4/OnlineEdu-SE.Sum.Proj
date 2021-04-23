package com.se231.onlineedu.controller;

import com.se231.onlineedu.jwtTest.UserDetailsDummy;
import com.se231.onlineedu.message.response.PersonalInfo;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.WebSecurityConfig;
import com.se231.onlineedu.security.jwt.JwtAuthEntryPoint;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.security.services.UserDetailsServiceImpl;
import com.se231.onlineedu.service.EmailSenderService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.service.VerificationTokenService;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author yuxuanLiu
 * @date 2019/07/22
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @TestConfiguration
    static class asdff{
        @Bean
        public JwtProvider jwtProvider(){
            return new JwtProvider();
        }

        @Bean
        public UserDetailsService userDetailsService(){
            return new UserDetailsServiceImpl();
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailSenderService emailSenderService;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @Mock
    private Clock clockMocker;

    @Autowired
    private JwtProvider jwtProvider;

    private String token;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(jwtProvider, "jwtExpiration", 3600000); // an hour
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret","mySecret");

        when(clockMocker.now())
                .thenReturn(DateUtil.now());

        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setRoles(List.of(new Role(RoleType.ROLE_TEACHING_ADMIN), new Role(RoleType.ROLE_ADMIN)));
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(1L)).thenReturn(userOptional);
        when(userRepository.findByUsername("john")).thenReturn(userOptional);

        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(2L);
        Course course3 = new Course();
        course3.setId(3L);
        user.setTeachCourses(List.of(course1, course2));
        user.setAssistCourses(List.of(course2, course3));
        Learn learn = new Learn();
        LearnPrimaryKey learnPrimaryKey = new LearnPrimaryKey();
        learnPrimaryKey.setCourse(course3);
        learnPrimaryKey.setStudent(user);
        learn.setLearnPrimaryKey(learnPrimaryKey);
        user.setLearns(List.of(learn));

        given(userService.getUserInfo(1L)).willReturn(user);

        token = getToken("john");

        user.setTel(123456L);
        when(userService.manageUserInfo(anyLong(), any(PersonalInfo.class))).thenReturn(user);

        when(userService.getAllUser()).thenReturn(List.of(user));

        when(userService.checkSameUsername(anyString())).thenReturn(true);

        when(userService.checkSameEmail(anyString())).thenReturn(true);

        when(userService.checkSameTel(anyString())).thenReturn(true);
    }

    private String getToken(String name) {
        return "Bearer " + jwtProvider.generateJwtToken(new UserDetailsDummy(name));
    }

    @Test
    public void getUserInfo() throws Exception {
        mvc.perform(get("/api/users/info")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getPersonalInfoTeach() throws Exception {
        mvc.perform(get("/api/users/info/courses/teach")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void getPersonalInfoAssist() throws Exception {
        mvc.perform(get("/api/users/info/courses/assist")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    public void getPersonalInfoLearn() throws Exception {
        mvc.perform(get("/api/users/info/courses/learn")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3));
    }

    @Test
    public void modifyPersonInfo() throws Exception {
        mvc.perform(post("/api/users/1/info/modify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tel\":\"18621107375\"}")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tel").value(123456));
    }

    @Test
    public void managePersonalInfo() throws Exception {
        mvc.perform(post("/api/users/info/modify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tel\":\"18621107375\"}")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tel").value(123456));
    }


    @Test
    public void getAllUsers() throws Exception {
        mvc.perform(get("/api/users/info/all")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void checkSameUsername() throws Exception {
        mvc.perform(get("/api/users/checkSame/username").param("username","username"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void checkSameEmail() throws Exception {
        mvc.perform(get("/api/users/checkSame/email").param("email","email"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void checkSameTel() throws Exception {
        mvc.perform(get("/api/users/checkSame/tel").param("tel","18621107375"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}

