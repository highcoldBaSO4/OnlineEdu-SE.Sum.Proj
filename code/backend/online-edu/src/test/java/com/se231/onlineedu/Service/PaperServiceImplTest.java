package com.se231.onlineedu.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.NotMatchException;
import com.se231.onlineedu.message.request.PaperForm;
import com.se231.onlineedu.message.request.PaperQuestionForm;
import com.se231.onlineedu.message.response.PaperFinish;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.*;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.PaperService;
import com.se231.onlineedu.serviceimpl.PaperServiceImpl;
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
 * @date 2019/07/31
 */
@RunWith(SpringRunner.class)
public class PaperServiceImplTest {
    @TestConfiguration
    static class PaperServiceImplTestContextConfig{
        @Bean
        public PaperService paperService(){
            return new PaperServiceImpl();
        }
    }

    @Autowired
    PaperService paperService;

    @MockBean
    private PaperWithQuestionsRepository paperWithQuestionsRepository;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private PaperRepository paperRepository;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PaperAnswerRepository paperAnswerRepository;

    private static Paper paper;

    private static Question question;

    private static Course course;

    private static User user;

    @Before
    public void init(){
        paper = new Paper();
        paper.setId(1L);

        course = new Course();
        course.setId(1L);
        paper.setCourse(course);

        question = new Question();
        question.setId(1L);
        question.setQuestionType(QuestionType.SINGLE_ANSWER);
        question.setQuestion("shit\0\r");

        user = new User();
        user.setId(1L);
    }

    @Test
    public void addNewPaperTest() throws Exception{
        //initialize paper info
        PaperQuestionForm paperQuestionForm = new PaperQuestionForm();
        paperQuestionForm.setQuestionId(1L);
        paperQuestionForm.setQuestionNumber(1);
        paperQuestionForm.setScore(1);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,5);
        PaperForm paperForm = new PaperForm();
        paperForm.setQuestionFormList(List.of(paperQuestionForm));
        paperForm.setStart(calendar.getTime());
        calendar.add(Calendar.DATE,5);
        paperForm.setEnd(calendar.getTime());
        paperForm.setTitle("test");

        // mockito
        Mockito.when(paperRepository.save(any(Paper.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Mockito.when(paperWithQuestionsRepository.save(any(PaperWithQuestions.class))).thenAnswer(i -> i.getArguments()[0]);

        Paper paper2 = paperService.addNewPaper(paperForm,1L);

        assertThat(paper2.getQuestionList().get(0)).isEqualTo(question);
    }

    @Test(expected = NotFoundException.class)
    public void addPaperNotFound() throws Exception{
        Mockito.when(paperRepository.save(any(Paper.class))).thenAnswer(i -> i.getArguments()[0]);
        PaperQuestionForm paperQuestionForm = new PaperQuestionForm();
        paperQuestionForm.setQuestionId(2L);
        paperQuestionForm.setQuestionNumber(1);
        paperQuestionForm.setScore(1);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,5);
        PaperForm paperForm = new PaperForm();
        paperForm.setQuestionFormList(List.of(paperQuestionForm));
        paperForm.setStart(calendar.getTime());
        calendar.add(Calendar.DATE,5);
        paperForm.setEnd(calendar.getTime());
        paperForm.setTitle("test");

        Mockito.when(paperRepository.save(any(Paper.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        paperService.addNewPaper(paperForm,1L);
    }

    @Test(expected = NotFoundException.class)
    public void paperNotFound1(){
        course.setPapers(List.of(paper));
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.empty());

        paperService.getPaperFinish(1L,1L);
    }

    @Test
    public void notStartTest(){
        course.setPapers(List.of(paper));
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(userRepository.getOne(1L)).thenReturn(user);
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.empty());

        List<PaperFinish> paperFinish = paperService.getPaperFinish(1L,1L);

        assertThat(paperFinish.get(0).getState()).isEqualTo(PaperAnswerState.NOT_START);
    }

    @Test
    public void getPaperFinishTest(){
        course.setPapers(List.of(paper));
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(userRepository.getOne(1L)).thenReturn(user);
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.of(2));

        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,2);
        PaperAnswer paperAnswer = new PaperAnswer();
        paperAnswer.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey);
        paperAnswer.setState(PaperAnswerState.TEMP_SAVE);
        Mockito.when(paperAnswerRepository.getOne(paperAnswerPrimaryKey)).thenReturn(paperAnswer);

        List<PaperFinish> paperFinish = paperService.getPaperFinish(1L,1L);

        assertThat(paperFinish.get(0).getState()).isEqualTo(PaperAnswerState.TEMP_SAVE);
        assertThat(paperFinish.get(0).getTimes()).isEqualTo(2);
    }

    @Test(expected = NotFoundException.class)
    public void paperNotFound2(){
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.empty());

        paperService.getStudentFinish(1L,1L);
    }

    @Test(expected = NotMatchException.class)
    public void notMatchTest(){
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Paper paper2 = new Paper();
        Course course2 = new Course();
        course2.setId(2L);
        paper2.setCourse(course2);

        Mockito.when(paperRepository.findById(2L)).thenReturn(Optional.of(paper2));

        paperService.getStudentFinish(1L,2L);
    }

    @Test
    public void getStudentFinishTest(){
        course.setPapers(List.of(paper));
        Learn learn = new Learn(user,course);
        course.setLearns(List.of(learn));
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(userRepository.getOne(1L)).thenReturn(user);
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.of(2));

        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,2);
        PaperAnswer paperAnswer = new PaperAnswer();
        paperAnswer.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey);
        paperAnswer.setState(PaperAnswerState.TEMP_SAVE);
        Mockito.when(paperAnswerRepository.getOne(paperAnswerPrimaryKey)).thenReturn(paperAnswer);

        List<PaperFinish> paperFinish = paperService.getStudentFinish(1L,1L);

        assertThat(paperFinish.get(0).getState()).isEqualTo(PaperAnswerState.TEMP_SAVE);
        assertThat(paperFinish.get(0).getTimes()).isEqualTo(2);
    }


}
