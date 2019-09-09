package com.se231.onlineedu.controller;

import com.se231.onlineedu.jwtTest.UserDetailsDummy;
import com.se231.onlineedu.message.request.SignInCourseForm;
import com.se231.onlineedu.message.request.SignInUserForm;
import com.se231.onlineedu.message.response.PersonalInfo;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.WebSecurityConfig;
import com.se231.onlineedu.security.jwt.JwtAuthEntryPoint;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.security.services.UserDetailsServiceImpl;
import com.se231.onlineedu.service.CourseService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author yuxuanLiu
 * @date 2019/07/31
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(SignInController.class)
@Import(WebSecurityConfig.class)
public class SignInControllerTest {
    @MockBean
    private CourseService courseService;

    @MockBean
    private UserService userService;

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

        when(courseService.saveSignIn(anyLong(),any(SignInCourseForm.class))).thenReturn(course1);
        when(userService.saveUserSignIn(anyLong(), any(SignInUserForm.class))).thenReturn(user);
    }

    private String getToken(String name) {
        return "Bearer " + jwtProvider.generateJwtToken(new UserDetailsDummy(name));
    }


    @Test
    public void postSignIn() throws Exception {
        mvc.perform(post("/api/courses/1/signIns").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"startDate\":\"2019-10-12 00:00:00\",\"endDate\":\"2019-11-21 00:00:00\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));


    }

    @Test
    public void postSignUp() throws Exception {
        mvc.perform(post("/api/users/1/signIns/").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"courseId\":1,\"signInNo\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
