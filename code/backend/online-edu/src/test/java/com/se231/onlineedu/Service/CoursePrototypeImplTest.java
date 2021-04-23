package com.se231.onlineedu.Service;

import com.se231.onlineedu.message.request.TitleAndDes;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.ApplyRepository;
import com.se231.onlineedu.repository.CoursePrototypeRepository;
import com.se231.onlineedu.repository.ResourceRepository;
import com.se231.onlineedu.service.CoursePrototypeService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.serviceimpl.CoursePrototypeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class CoursePrototypeImplTest {

    @TestConfiguration
    static class CoursePrototypeServiceImplTestContextConfig{
        @Bean
        public CoursePrototypeService authService(){
            return new CoursePrototypeServiceImpl();
        }

    }

    @Autowired
    private CoursePrototypeService coursePrototypeService;

    @MockBean
    private UserService userService;


    @MockBean
    private CoursePrototypeRepository coursePrototypeRepository;

    @MockBean
    private ApplyRepository applyRepository;

    @MockBean
    private ResourceRepository resourceRepository;


    @Before
    public void setup(){
        Mockito.when(coursePrototypeRepository.save(any(CoursePrototype.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(applyRepository.save(any(Apply.class))).thenAnswer(i -> i.getArguments()[0]);

    }

    @Test
    public void createCourse(){
        TitleAndDes form = new TitleAndDes();
        form.setDescription("des");
        form.setTitle("123");


        User user = new User();
        user.setId(1L);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        CoursePrototype coursePrototype = coursePrototypeService.createCourse(form, 1L);
        assertThat(coursePrototype.getUsers().size()).isEqualTo(1L);
        assertThat(coursePrototype.getDescription()).isEqualTo("des");
    }

    @Test
    public void applyForCourse(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Optional<CoursePrototype> coursePrototypeOptional = Optional.of(coursePrototype);

        User user = new User();
        user.setId(1L);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(coursePrototypeRepository.findById(1L)).thenReturn(coursePrototypeOptional);
        Apply apply = coursePrototypeService.applyForCourse(1L,1L);
        assertThat(apply.getApplicationForCoursePK().getCoursePrototype().getId()).isEqualTo(1L);
    }

    @Test
    public void getCoursePrototypeInfo(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Optional<CoursePrototype> coursePrototypeOptional = Optional.of(coursePrototype);
        Mockito.when(coursePrototypeRepository.findById(1L)).thenReturn(coursePrototypeOptional);
        CoursePrototype found = coursePrototypeService.getCoursePrototypeInfo(1L);
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void decideCreateCourse(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Optional<CoursePrototype> coursePrototypeOptional = Optional.of(coursePrototype);
        Mockito.when(coursePrototypeRepository.findById(1L)).thenReturn(coursePrototypeOptional);
        CoursePrototype found = coursePrototypeService.decideCreateCourse(1L, "approval");
        assertThat(found.getState()).isEqualTo(CoursePrototypeState.USING);
        found = coursePrototypeService.decideCreateCourse(1L, "disapproval");
        assertThat(found.getState()).isEqualTo(CoursePrototypeState.DENIAL);
    }

    @Test
    public void decideUseCourse(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Optional<CoursePrototype> coursePrototypeOptional = Optional.of(coursePrototype);
        Mockito.when(coursePrototypeRepository.findById(1L)).thenReturn(coursePrototypeOptional);

        User user = new User();
        user.setId(1L);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        Apply apply = new Apply();
        ApplyPrimaryKey applyPrimaryKey = new ApplyPrimaryKey(user, coursePrototype);
        Optional<Apply> applyOption = Optional.of(apply);
        Mockito.when(applyRepository.findById(any(ApplyPrimaryKey.class))).thenReturn(applyOption);

        Apply found = coursePrototypeService.decideUseCourse(1L,1L,ApplyState.APPROVAL.name());
        assertThat(found.getState()).isEqualTo(ApplyState.APPROVAL);
    }

    @Test
    public void saveResource(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Optional<CoursePrototype> coursePrototypeOptional = Optional.of(coursePrototype);
        Mockito.when(coursePrototypeRepository.findById(1L)).thenReturn(coursePrototypeOptional);

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setUrl("123456");
        resource.setResourceType(ResourceType.PDF);
        CoursePrototype found = coursePrototypeService.saveResource(1L, resource);

        assertThat(found.getResources().size()).isEqualTo(1);
    }

    @Test
    public void getApplyByCoursePrototype(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Optional<CoursePrototype> coursePrototypeOptional = Optional.of(coursePrototype);
        Mockito.when(coursePrototypeRepository.findById(1L)).thenReturn(coursePrototypeOptional);

        User user = new User();
        user.setId(1L);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);

        Apply apply = new Apply();
        ApplyPrimaryKey applyPrimaryKey = new ApplyPrimaryKey(user, coursePrototype);
        apply.setApplicationForCoursePK(applyPrimaryKey);
        Mockito.when(applyRepository.findAppliesByPrototypeId(1L)).thenReturn(List.of(apply));

        List<Apply> found = coursePrototypeService.getApplyByCoursePrototype(1L);
        assertThat(found.size()).isEqualTo(1);
    }

    @Test
    public void getAllCoursePrototype(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        Mockito.when(coursePrototypeRepository.findAll()).thenReturn(List.of(coursePrototype));
        assertThat(coursePrototypeService.getAllCoursePrototype().size()).isEqualTo(1);
    }


}
