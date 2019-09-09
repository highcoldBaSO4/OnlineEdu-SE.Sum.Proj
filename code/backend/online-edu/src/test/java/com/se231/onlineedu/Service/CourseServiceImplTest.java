package com.se231.onlineedu.Service;

import com.se231.onlineedu.exception.CoursePrototypeUnavailableException;
import com.se231.onlineedu.exception.EndBeforeStartException;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.message.request.CourseApplicationForm;
import com.se231.onlineedu.message.request.CourseModifyForm;
import com.se231.onlineedu.message.request.SignInCourseForm;
import com.se231.onlineedu.message.response.CourseWithIdentity;
import com.se231.onlineedu.message.response.Identity;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.*;
import com.se231.onlineedu.service.CoursePrototypeService;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.serviceimpl.CourseServiceImpl;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class CourseServiceImplTest {

    @TestConfiguration
    static class CourseServiceImplTestConfig{
        @Bean
        public CourseService courseService(){
            return new CourseServiceImpl();
        }
    }
    @MockBean
    private NoticeRepository noticeRepository;


    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @MockBean
    private CoursePrototypeService coursePrototypeService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private TimeSlotRepository timeSlotRepository;

    @MockBean
    private LearnRepository learnRepository;

    @MockBean
    private SignInRepository signInRepository;

    @Before
    public void setup(){
        Mockito.when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(learnRepository.save(any(Learn.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(signInRepository.save(any(SignIn.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void applyToStartCourse(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setTitle("title");
        coursePrototype.setState(CoursePrototypeState.USING);
        coursePrototype.setId(2L);

        CourseApplicationForm form = new CourseApplicationForm();
        form.setCourseTitle("lal");
        form.setEndDate(DateUtil.tomorrow());
        form.setStartDate(DateUtil.yesterday());

        User user = new User();
        user.setId(12L);

        Mockito.when(coursePrototypeService.getCoursePrototypeInfo(2L)).thenReturn(coursePrototype);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        Course course = courseService.applyToStartCourse(form,2L,1L);
        assertThat(course.getTeacher().getId()).isEqualTo(12L);
        assertThat(course.getCourseTitle()).isEqualTo("lal");
        assertThat(course.getCoursePrototype().getId()).isEqualTo(2L);
    }

    @Test(expected = EndBeforeStartException.class)
    public void applyToCourseEndBeforeStart(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setTitle("title");
        coursePrototype.setState(CoursePrototypeState.USING);
        coursePrototype.setId(2L);

        CourseApplicationForm form = new CourseApplicationForm();
        form.setCourseTitle("lal");
        form.setEndDate(DateUtil.yesterday());
        form.setStartDate(DateUtil.tomorrow());

        User user = new User();
        user.setId(12L);

        Mockito.when(coursePrototypeService.getCoursePrototypeInfo(2L)).thenReturn(coursePrototype);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        courseService.applyToStartCourse(form,2L,1L);
    }

    @Test(expected = CoursePrototypeUnavailableException.class)
    public void applyToCourseUnavailableCourseProtoType(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setTitle("title");
        coursePrototype.setState(CoursePrototypeState.DENIAL);
        coursePrototype.setId(2L);

        CourseApplicationForm form = new CourseApplicationForm();
        form.setCourseTitle("lal");
        form.setEndDate(DateUtil.tomorrow());
        form.setStartDate(DateUtil.yesterday());

        User user = new User();
        user.setId(12L);

        Mockito.when(coursePrototypeService.getCoursePrototypeInfo(2L)).thenReturn(coursePrototype);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        courseService.applyToStartCourse(form,2L,1L);
    }

    @Test
    public void pickCourse(){
        User user = new User();
        user.setId(12L);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        Course course = new Course();
        course.setId(56L);
        Optional<Course> courseOptional = Optional.of(course);
        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);

        Learn learn = courseService.pickCourse(1L,56L);
        assertThat(learn.getLearnPrimaryKey().getCourse().getId()).isEqualTo(56L);
        assertThat(learn.getLearnPrimaryKey().getStudent().getId()).isEqualTo(12L);
    }

    @Test
    public void getStudentInfo(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        course.setLearns(List.of(learn));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);

        List<User> students = courseService.getStudentsList(56L);
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getId()).isEqualTo(45L);
    }

    @Test(expected = NotFoundException.class)
    public void getCourseInfoException(){
        Mockito.when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        courseService.getCourseInfo(12L);

    }

    @Test
    public void getCourseInfo(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        course.setLearns(List.of(learn));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        Course found = courseService.getCourseInfo(56L);
        assertThat(found.getStudents().get(0).getId()).isEqualTo(45L);
    }

    @Test
    public void getCourseInfoWithIdentityStudent(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        course.setLearns(List.of(learn));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        CourseWithIdentity courseWithIdentity = courseService.getCourseInfoWithIdentity(56L, 45L);

        assertThat(courseWithIdentity.getIdentity()).isEqualTo(Identity.STUDENT);
    }

    @Test
    public void getCourseInfoWithIdentityTeacherAssistant(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        course.setTeacherAssistants(List.of(user));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        CourseWithIdentity courseWithIdentity = courseService.getCourseInfoWithIdentity(56L, 45L);

        assertThat(courseWithIdentity.getIdentity()).isEqualTo(Identity.TEACHER_ASSISTANT);
    }


    @Test
    public void getCourseInfoWithIdentityTeacher(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        course.setTeacher(user);
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        CourseWithIdentity courseWithIdentity = courseService.getCourseInfoWithIdentity(56L, 45L);

        assertThat(courseWithIdentity.getIdentity()).isEqualTo(Identity.TEACHER);
    }

    @Test
    public void getAllCourses(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        course.setTeacher(user);

        Mockito.when(courseRepository.findAll()).thenReturn(List.of(course));

        List<Course> courses = courseService.getAllCourse();
        assertThat(courses.size()).isEqualTo(1);
        assertThat(courses.get(0).getTeacher().getId()).isEqualTo(45L);
    }

    @Test
    public void updateCourseAvatar(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        course.setTeacher(user);
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);

        Course found = courseService.updateCourseAvatar("url",56L);
        assertThat(found.getAvatarUrl()).isEqualTo("url");
    }

    @Test
    public void getTeacherAssistantAndTeacherEmail(){
        Course course = new Course();
        course.setId(56L);
        User teacher = new User();
        teacher.setId(45L);
        teacher.setEmail("teacher");
        User teacherAssistant = new User();
        teacherAssistant.setId(66L);
        teacherAssistant.setEmail("teacherAssistant");
        course.setTeacher(teacher);
        course.setTeacherAssistants(List.of(teacherAssistant));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        List<String> emails = courseService.getTeacherAssistantAndTeacherEmail(56L);
        assertThat(emails).isEqualTo(List.of("teacher","teacherAssistant"));
    }

    @Test
    public void checkIfPicked(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        course.setLearns(List.of(learn));
        user.setLearns(List.of(learn));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(userService.getUserInfo(45L)).thenReturn(user);
        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        assertThat(courseService.checkIfUserPick(56L,45L)).isTrue();
    }

    @Test
    public void modifyCourseInfo(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        course.setLearns(List.of(learn));
        user.setLearns(List.of(learn));
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(userService.getUserInfo(45L)).thenReturn(user);
        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        CourseModifyForm form = new CourseModifyForm();
        form.setLocation("RY1101");
        Course found = courseService.modifyCourseInfo(56L, form);
        assertThat(found.getLocation()).isEqualTo("RY1101");
    }

    @Test
    public void selectTeacherAssistant(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        List<Learn> learns = new ArrayList<>();
        learns.add(learn);
        course.setLearns(learns);
        user.setLearns(learns);
        Optional<Course> courseOptional = Optional.of(course);

        Mockito.when(userService.getUserInfo(45L)).thenReturn(user);
        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);


        Course found = courseService.selectTeacherAssistant(56L, 45L);
        assertThat(found.getLearns().size()).isEqualTo(0);
        assertThat(found.getTeacherAssistants().size()).isEqualTo(1);
    }

    @Test
    public void saveSignIn(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        List<Learn> learns = new ArrayList<>();
        learns.add(learn);
        course.setLearns(learns);
        user.setLearns(learns);
        Optional<Course> courseOptional = Optional.of(course);
        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);
        SignInCourseForm signInCourseForm = new SignInCourseForm();
        signInCourseForm.setSignInNo(12);
        signInCourseForm.setStartDate(DateUtil.yesterday());
        signInCourseForm.setEndDate(DateUtil.tomorrow());

        Course found = courseService.saveSignIn(56L, signInCourseForm);
        assertThat(found.getSignIns().size()).isEqualTo(1);
        assertThat(found.getSignIns().get(0).getSignInPrimaryKey().getSignInNo()).isEqualTo(12);
    }

    @Test
    public void setState(){
        Course course = new Course();
        course.setId(56L);
        User user = new User();
        user.setId(45L);
        Learn learn = new Learn(new LearnPrimaryKey(user, course),12,1);
        List<Learn> learns = new ArrayList<>();
        learns.add(learn);
        course.setLearns(learns);
        user.setLearns(learns);
        Optional<Course> courseOptional = Optional.of(course);
        Mockito.when(courseRepository.findById(56L)).thenReturn(courseOptional);

        Course found = courseService.setState(56L, CourseState.NOT_PASS.name());
        assertThat(found.getState()).isEqualTo(CourseState.NOT_PASS);
    }
}
