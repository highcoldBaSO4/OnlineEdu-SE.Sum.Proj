package com.se231.onlineedu.controller;

import com.se231.onlineedu.jwtTest.UserDetailsDummy;
import com.se231.onlineedu.message.request.CourseApplicationForm;
import com.se231.onlineedu.message.request.CourseModifyForm;
import com.se231.onlineedu.message.response.CourseWithIdentity;
import com.se231.onlineedu.message.response.GradeTable;
import com.se231.onlineedu.message.response.Identity;
import com.se231.onlineedu.message.response.StudentAndGrade;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.WebSecurityConfig;
import com.se231.onlineedu.security.jwt.JwtAuthEntryPoint;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.security.services.UserDetailsServiceImpl;
import com.se231.onlineedu.service.CourseService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author yuxuanLiu
 * @date 2019/07/30
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(CourseController.class)
@Import(WebSecurityConfig.class)
public class CourseControllerTest {

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
    private CourseService courseService;


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

        CourseWithIdentity courseWithIdentity = new CourseWithIdentity();
        courseWithIdentity.setIdentity(Identity.STUDENT);
        courseWithIdentity.setCourse(course1);
        when(courseService.saveNotice(anyLong(), any(Notice.class))).thenReturn(course1);
        when(courseService.applyToStartCourse(any(CourseApplicationForm.class), anyLong(), anyLong())).thenReturn(course1);
        when(courseService.examineStartCourseApplication(anyLong(), any(String.class))).thenReturn(course1);
        when(courseService.pickCourse(anyLong(),anyLong())).thenReturn(learn);
        when(courseService.getStudentsList(1L)).thenReturn(List.of(user));
        when(courseService.getCourseInfoWithIdentity(anyLong(),anyLong())).thenReturn(courseWithIdentity);
        when(courseService.getAllCourse()).thenReturn(List.of(course1,course2,course3));
        course1.setAvatarUrl("avatarUrl");
        when(courseService.updateCourseAvatar(anyString(), anyLong())).thenReturn(course1);
        when(courseService.checkIfUserPick(anyLong(),anyLong())).thenReturn(true);
        when(courseService.modifyCourseInfo(anyLong(),any(CourseModifyForm.class))).thenReturn(course1);
        when(courseService.selectTeacherAssistant(anyLong(),anyLong())).thenReturn(course1);

        GradeTable gradeTable = new GradeTable();
        gradeTable.setCourse(course1);
        StudentAndGrade studentAndGrade = new StudentAndGrade();
        studentAndGrade.setScore(12D);
        studentAndGrade.setStudent(user);
        gradeTable.setScoreMap(List.of(studentAndGrade));
        when(courseService.getGrade(anyLong())).thenReturn(gradeTable);

        token = getToken("john");
    }

    private String getToken(String name) {
        return "Bearer " + jwtProvider.generateJwtToken(new UserDetailsDummy(name));
    }

    @Test
    public void uploadNotice() throws Exception {
        Course course = new Course();
        course.setId(45L);
        mvc.perform(post("/api/courses/45/notices/").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"haha\",\"content\":\"really?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notices[0].title").value("title"));
    }


    @Test
    public void checkIfUserPick() throws Exception {
        mvc.perform(get("/api/courses/45/isPicked").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void applyToStartCourse() throws Exception {
//        mvc.perform(post("/api/courses/start").param("prototypeId", "1").header("Authorization", token).contentType(MediaType.APPLICATION_JSON)
//        .content("{\"courseTitle\":\"hah\",\"startDate\":\"2019-09-30 00:00:00\",\"endDate\":\"2019-10-18 00:00:00\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1));
//    }

    @Test
    public void examineStartCourse() throws Exception {
        mvc.perform(post("/api/courses/1/start").header("Authorization", token).param("decision", "approval"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void pickCourse() throws Exception {
        mvc.perform(post("/api/courses/1/pick").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentsList() throws Exception {
        mvc.perform(get("/api/courses/1/students").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void getCourseInfo() throws Exception {
        mvc.perform(get("/api/courses/1/info").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identity").value("STUDENT"));
    }

    @Test
    public void getAllCourses() throws Exception {
        mvc.perform(get("/api/courses/all/info").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void checkWhetherPicked() throws Exception {
        mvc.perform(get("/api/courses/1/isPicked").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void modifyCourseInfo() throws Exception {
        mvc.perform(put("/api/courses/1/modify").header("Authorization", token).contentType(MediaType.APPLICATION_JSON)
        .content("{\"courseTitle\":\"hah\",\"startDate\":\"2019-09-30 00:00:00\",\"endDate\":\"2019-10-18 00:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void selectTA() throws Exception {
        mvc.perform(post("/api/courses/1/teacherAssistant").param("teacherAssistantId","1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void scoreList() throws Exception {
        mvc.perform(get("/api/courses/1/scoreList").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMap",hasSize(1)));
    }
}
