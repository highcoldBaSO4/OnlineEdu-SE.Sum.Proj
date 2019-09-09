package com.se231.onlineedu.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import com.se231.onlineedu.exception.ValidationException;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.PaperRepository;
import com.se231.onlineedu.repository.ResourceRepository;
import com.se231.onlineedu.repository.SectionBranchRepository;
import com.se231.onlineedu.repository.SectionRepository;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.SectionService;
import com.se231.onlineedu.serviceimpl.SectionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zhe Li
 * @date 2019/08/02
 */
@RunWith(SpringRunner.class)
public class SectionServiceImplTest {

    @TestConfiguration
    static class SectionServiceImplTestContextConfig{
        @Bean
        public SectionService questionService(){
            return new SectionServiceImpl();
        }
    }

    @Autowired
    SectionService sectionService;

    @MockBean
    CourseService courseService;

    @MockBean
    SectionRepository sectionRepository;

    @MockBean
    SectionBranchRepository sectionBranchRepository;

    @MockBean
    PaperRepository paperRepository;

    @MockBean
    ResourceRepository resourceRepository;

    @MockBean
    EntityManager entityManager;

    private static Section section1;
    private static Section section2;

    private static SectionBranches sectionBranches1;
    private static SectionBranches sectionBranches2;

    private static Course course;

    @Before
    public void init(){
        course = new Course();
        course.setId(1L);

        section1 = new Section();
        section1.setSecNo(1);
        section1.setSectionPrimaryKey(new SectionPrimaryKey(course,1));

        section2 = new Section();
        section2.setSecNo(2);
        section2.setSectionPrimaryKey(new SectionPrimaryKey(course,2));

        sectionBranches1 = new SectionBranches();
        sectionBranches1.setBranchNo(1);
        sectionBranches1.setSectionBranchesPrimaryKey(new SectionBranchesPrimaryKey(section1,1));

        sectionBranches2 = new SectionBranches();
        sectionBranches2.setBranchNo(2);
        sectionBranches2.setSectionBranchesPrimaryKey(new SectionBranchesPrimaryKey(section2,2));

        section1.setSectionBranchesList(List.of(sectionBranches1,sectionBranches2));
        course.setSectionList(List.of(section1,section2));
    }

    @Test(expected = ValidationException.class)
    public void invalidSectionNumberTest1(){
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(sectionRepository.currentSecNo(1L)).thenReturn(Optional.of(2));

        sectionService.createSection(1L,3,"title");
    }

    @Test(expected = ValidationException.class)
    public void invalidSectionNumberTest2(){
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(sectionRepository.currentSecNo(1L)).thenReturn(Optional.of(2));

        sectionService.createSection(1L,-1,"title");
    }

    @Test
    public void createSectionTest(){
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(sectionRepository.currentSecNo(1L)).thenReturn(Optional.of(2));

    }
}
