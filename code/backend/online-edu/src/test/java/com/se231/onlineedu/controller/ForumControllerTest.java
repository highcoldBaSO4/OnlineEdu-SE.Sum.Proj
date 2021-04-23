package com.se231.onlineedu.controller;

import com.se231.onlineedu.jwtTest.UserDetailsDummy;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.WebSecurityConfig;
import com.se231.onlineedu.security.jwt.JwtAuthEntryPoint;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.security.services.UserDetailsServiceImpl;
import com.se231.onlineedu.service.*;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author yuxuanLiu
 * @date 2019/07/31
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(ForumController.class)
@Import(WebSecurityConfig.class)
public class ForumControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;


    @MockBean
    private DiscernSensitiveWordsService discernSensitiveWordsService;

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
    private ForumService forumService;

    @Mock
    private Clock clockMocker;

    @Autowired
    private JwtProvider jwtProvider;

    private String token;

    @MockBean
    private EmailSenderService emailSenderService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private WordCloudService wordCloudService;

    @MockBean
    private LearnService learnService;


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

        Forum forum = new Forum();
        forum.setUserId(1L);
        forum.setContent("whatever");
        forum.setCourseId(2L);
        forum.setTitle("title");

        when(forumService.updateForum(any(Forum.class))).thenAnswer(i->i.getArguments()[0]);
        when(forumService.getForumsByCourse(anyLong())).thenReturn(List.of(forum));
        when(forumService.getForumsBySection(anyLong(),anyInt())).thenReturn(List.of(forum));
        when(forumService.getForum(anyString())).thenReturn(forum);

        token = getToken("john");
    }

    private String getToken(String name) {
        return "Bearer " + jwtProvider.generateJwtToken(new UserDetailsDummy(name));
    }

    @Test
    public void insertForum() throws Exception {
        mvc.perform(post("/api/courses/1/sections/1/forums/").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"content\",\"title\":\"title\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    public void getForumsByCourse() throws Exception {
        mvc.perform(get("/api/courses/1/forums/").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"content\",\"title\":\"title\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @Test
    public void getForumsBySection() throws Exception {
        mvc.perform(get("/api/courses/1/sections/1/forums/").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"content\",\"title\":\"title\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @Test
    public void getForumsById() throws Exception {
        mvc.perform(get("/api/forums/798").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"content\",\"title\":\"title\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
    }


    @Test
    public void deleteForum() throws Exception {
        mvc.perform(delete("/api/forums/987").header("Authorization", token))
                .andDo(print())
                .andExpect(jsonPath("$.title").value("该内容已被锁"));
    }
}
