package com.se231.onlineedu.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import java.util.*;
import com.se231.onlineedu.exception.*;
import com.se231.onlineedu.message.request.MarkForm;
import com.se231.onlineedu.message.request.QuestionAnswer;
import com.se231.onlineedu.message.request.SubmitAnswerForm;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.*;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.PaperAnswerService;
import com.se231.onlineedu.service.PaperService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.serviceimpl.PaperAnswerServiceImpl;
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
 * @date 2019/07/30
 */
@RunWith(SpringRunner.class)
public class PaperAnswerServiceImplTest {
    @TestConfiguration
    static class UserServiceImplTestContextConfig{
        @Bean
        public PaperAnswerService paperAnswerService(){
            return new PaperAnswerServiceImpl();
        }
    }

    @Autowired
    PaperAnswerService paperAnswerService;

    @MockBean
    UserService userService;

    @MockBean
    PaperService paperService;

    @MockBean
    CourseService courseService;

    @MockBean
    PaperRepository paperRepository;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    AnswerRepository answerRepository;

    @MockBean
    PaperAnswerRepository paperAnswerRepository;

    @MockBean
    PaperWithQuestionsRepository paperWithQuestionsRepository;

    private static User user;

    private static Paper paper;

    private static Question question;

    private static Course course;

    private static SubmitAnswerForm submitAnswerForm;

    @Before
    public void setup(){
        user = new User();
        user.setId(1L);

        paper = new Paper();
        paper.setId(1L);

        question = new Question();
        question.setId(1L);

        course = new Course();
        course.setId(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-5);
        paper.setCourse(course);
        paper.setStart(calendar.getTime());
        calendar.add(Calendar.DATE,15);
        paper.setEnd(calendar.getTime());

        QuestionAnswer questionAnswer = new QuestionAnswer(question.getId(),"A");
        List<QuestionAnswer> questionAnswers = new ArrayList<>();
        questionAnswers.add(questionAnswer);
        submitAnswerForm = new SubmitAnswerForm(questionAnswers,"FINISHED");

        PaperWithQuestions paperWithQuestions = new PaperWithQuestions(paper,question,1,1.0);
        paper.setQuestions(List.of(paperWithQuestions));
    }

    @Test(expected = BeforeStartException.class)
    public void beforeStartTest()throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,5);
        paper.setStart(calendar.getTime());
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.empty());
        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Mockito.when(paperRepository.getOne(1L)).thenReturn(paper);

        PaperAnswer paperAnswer = paperAnswerService.submitAnswer(1L,1L,1L,submitAnswerForm);
        assertThat(paperAnswer.getPaperAnswerPrimaryKey().getTimes()).isEqualTo(1);
    }

    @Test(expected = AfterEndException.class)
    public void afterEndTest()throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-5);
        paper.setEnd(calendar.getTime());
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.empty());
        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Mockito.when(paperRepository.getOne(1L)).thenReturn(paper);

        PaperAnswer paperAnswer = paperAnswerService.submitAnswer(1L,1L,1L,submitAnswerForm);
        assertThat(paperAnswer.getPaperAnswerPrimaryKey().getTimes()).isEqualTo(1);
    }

    @Test(expected = ValidationException.class)
    public void answerTimesTest()throws Exception{
        Mockito.when(paperAnswerRepository.getMaxTimes(user.getId(),paper.getId())).thenReturn(Optional.of(3));
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,3);
        PaperAnswer paperAnswer = new PaperAnswer(paperAnswerPrimaryKey);
        paperAnswer.setState(PaperAnswerState.FINISHED);
        Mockito.when(paperAnswerRepository.getOne(paperAnswerPrimaryKey)).thenReturn(paperAnswer);
        paperAnswerService.submitAnswer(user.getId(),course.getId(),paper.getId(),submitAnswerForm);
    }

    @Test
    public void submitAnswer()throws Exception{
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.empty());
        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Mockito.when(paperRepository.getOne(1L)).thenReturn(paper);

        PaperAnswer paperAnswer = paperAnswerService.submitAnswer(1L,1L,1L,submitAnswerForm);
        assertThat(paperAnswer.getPaperAnswerPrimaryKey().getTimes()).isEqualTo(1);
    }

    @Test
    public void notFinishTest()throws Exception{
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.of(2));
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        PaperAnswer paperAnswer1 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,2);
        paperAnswer1.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey);
        paperAnswer1.setState(PaperAnswerState.NOT_FINISH);
        Answer answer = new Answer();
        AnswerPrimaryKey answerPrimaryKey = new AnswerPrimaryKey(paperAnswer1,question);
        answer.setAnswerPrimaryKey(answerPrimaryKey);
        answer.setAnswer("A");
        paperAnswer1.setAnswers(List.of(answer));

        Mockito.when(paperAnswerRepository.getOne(paperAnswerPrimaryKey)).thenReturn(paperAnswer1);
        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(answerRepository.save(any(Answer.class))).thenAnswer(i->i.getArguments()[0]);
        Mockito.when(paperRepository.getOne(paper.getId())).thenReturn(paper);

        PaperAnswer paperAnswer2 = paperAnswerService.submitAnswer(1L,1L,1L,submitAnswerForm);
        assertThat(paperAnswer2.getPaperAnswerPrimaryKey().getTimes()).isEqualTo(2);
        assertThat(paperAnswer2.getAnswers().size()).isEqualTo(1);
    }

    @Test
    public void tempSaveTest()throws Exception{
        // mockito in get paper answer
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.of(2));
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        PaperAnswer paperAnswer1 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,2);
        paperAnswer1.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey);
        paperAnswer1.setState(PaperAnswerState.TEMP_SAVE);
        Mockito.when(paperRepository.getOne(1L)).thenReturn(paper);
        Answer answer = new Answer();
        AnswerPrimaryKey answerPrimaryKey = new AnswerPrimaryKey(paperAnswer1,question);
        answer.setAnswerPrimaryKey(answerPrimaryKey);
        answer.setAnswer("A");
        paperAnswer1.setAnswers(new ArrayList<>(List.of(answer)));

        Mockito.when(paperAnswerRepository.getOne(paperAnswerPrimaryKey)).thenReturn(paperAnswer1);
        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(answerRepository.save(any(Answer.class))).thenAnswer(i->i.getArguments()[0]);

        PaperAnswer paperAnswer2 = paperAnswerService.submitAnswer(1L,1L,1L,submitAnswerForm);
        assertThat(paperAnswer2.getPaperAnswerPrimaryKey().getTimes()).isEqualTo(2);
        assertThat(paperAnswer2.getAnswers().size()).isEqualTo(2);
    }

    @Test(expected = AnswerException.class)
    public void questionNotMatchTest()throws Exception{
        // mockito in get paper answer
        Mockito.when(paperAnswerRepository.getMaxTimes(1L,1L)).thenReturn(Optional.of(2));
        Mockito.when(paperService.getPaperInfo(1L,1L)).thenReturn(paper);
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(courseService.getCourseInfo(1L)).thenReturn(course);
        PaperAnswer paperAnswer1 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,2);
        paperAnswer1.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey);
        paperAnswer1.setState(PaperAnswerState.FINISHED);
        Mockito.when(paperAnswerRepository.getOne(new PaperAnswerPrimaryKey(user,paper,2))).thenReturn(paperAnswer1);
        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);

        Mockito.when(paperRepository.getOne(1L)).thenReturn(paper);

        Question question2 = new Question();
        question2.setId(2L);
        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));

        QuestionAnswer questionAnswer = new QuestionAnswer(2L,"A");
        SubmitAnswerForm submitAnswerForm = new SubmitAnswerForm(List.of(questionAnswer),"FINISHED");
        paperAnswerService.submitAnswer(1L,1L,1L,submitAnswerForm);

    }

    @Test
    public void autoMarkTest(){
        //initialize paper and questions
        Paper paper2 = new Paper();
        paper2.setId(2L);

        Question obj1 = new Question();
        obj1.setId(2L);
        obj1.setAnswer("A");
        obj1.setQuestionType(QuestionType.SINGLE_ANSWER);

        Question obj2 = new Question();
        obj2.setId(3L);
        obj2.setAnswer("AB");
        obj2.setQuestionType(QuestionType.MULTIPLE_ANSWER);

        Question sub1 = new Question();
        sub1.setId(4L);
        sub1.setQuestionType(QuestionType.SUBJECTIVE);

        List<PaperWithQuestions> paperWithQuestionsList = new ArrayList<>();
        paperWithQuestionsList.add(new PaperWithQuestions(paper2,obj1,1,5.0D));
        paperWithQuestionsList.add(new PaperWithQuestions(paper2,obj2,2,5.0D));
        paperWithQuestionsList.add(new PaperWithQuestions(paper2,sub1,3,10.0D));
        paper2.setQuestions(paperWithQuestionsList);

        //initialize paper answer
        PaperAnswer paperAnswer1 = new PaperAnswer();
        paperAnswer1.setState(PaperAnswerState.FINISHED);

        AnswerPrimaryKey answer1 = new AnswerPrimaryKey(paperAnswer1,obj1);
        AnswerPrimaryKey answer2 = new AnswerPrimaryKey(paperAnswer1,obj2);
        Answer wrong1 = new Answer(answer1,"B",0D);
        Answer right2 = new Answer(answer2,"AB",0D);

        paperAnswer1.setAnswers(List.of(wrong1,right2));

        PaperAnswer paperAnswer2 = new PaperAnswer();
        paperAnswer1.setState(PaperAnswerState.FINISHED);

        AnswerPrimaryKey answer3 = new AnswerPrimaryKey(paperAnswer2,obj1);
        AnswerPrimaryKey answer4 = new AnswerPrimaryKey(paperAnswer2,obj2);
        Answer right3 = new Answer(answer3,"A",0D);
        Answer right4 = new Answer(answer4,"AB",0D);

        paperAnswer2.setAnswers(List.of(right3,right4));

        Paper paper3 = new Paper();
        paper3.setId(3L);
        List<PaperWithQuestions> paperWithQuestionsList2 = new ArrayList<>();
        paperWithQuestionsList2.add(new PaperWithQuestions(paper3,obj1,1,5.0D));
        paperWithQuestionsList2.add(new PaperWithQuestions(paper3,obj2,2,5.0D));
        paper3.setQuestions(paperWithQuestionsList2);

        PaperAnswer paperAnswer3 = new PaperAnswer();
        paperAnswer3.setState(PaperAnswerState.FINISHED);

        AnswerPrimaryKey answer5 = new AnswerPrimaryKey(paperAnswer3,obj1);
        AnswerPrimaryKey answer6 = new AnswerPrimaryKey(paperAnswer3,obj2);
        Answer right5 = new Answer(answer5,"A",0D);
        Answer wrong6 = new Answer(answer6,"BC",0D);

        paperAnswer3.setAnswers(List.of(right5,wrong6));

        //Mockito paper
        Mockito.when(paperRepository.getOne(2L)).thenReturn(paper2);
        Mockito.when(paperRepository.getOne(3L)).thenReturn(paper3);

        //Mockito paper answer
        Mockito.when(paperAnswerRepository.getPaperAnswers(2L)).thenReturn(List.of(paperAnswer1,paperAnswer2));
        Mockito.when(paperAnswerRepository.getPaperAnswers(3L)).thenReturn(List.of(paperAnswer3));

        //invoke function
        paperAnswerService.autoMark(2L);
        paperAnswerService.autoMark(3L);

        // grade assert
        assertThat(wrong1.getGrade()).isEqualTo(0D);
        assertThat(right2.getGrade()).isEqualTo(5.0D);
        assertThat(right3.getGrade()).isEqualTo(5.0D);
        assertThat(right4.getGrade()).isEqualTo(5.0D);
        assertThat(right5.getGrade()).isEqualTo(5.0D);
        assertThat(wrong6.getGrade()).isEqualTo(0D);

        assertThat(paperAnswer1.getGrade()).isEqualTo(5.0D);
        assertThat(paperAnswer2.getGrade()).isEqualTo(10.0D);
        assertThat(paperAnswer3.getGrade()).isEqualTo(5.0D);

        //state assert
        assertThat(paperAnswer1.getState()).isEqualTo(PaperAnswerState.NOT_MARKED);
        assertThat(paperAnswer3.getState()).isEqualTo(PaperAnswerState.MARKED);
    }

    @Test
    public void getPersonalAnswerTest(){
        PaperAnswer paperAnswer1 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey1 = new PaperAnswerPrimaryKey(user,paper,1);
        paperAnswer1.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey1);
        PaperAnswer paperAnswer2 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey2 = new PaperAnswerPrimaryKey(user,paper,2);
        paperAnswer2.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey2);

        Mockito.when(paperAnswerRepository.getPersonalPaperAnswer(1L,1L)).
                thenReturn(List.of(paperAnswer1,paperAnswer2));

        List<PaperAnswer> paperAnswerList = paperAnswerService.getPersonalPaperAnswer(1L,1L);
        assertThat(paperAnswerList).contains(paperAnswer1);
        assertThat(paperAnswerList).contains(paperAnswer2);
    }

    @Test(expected = NotFoundException.class)
    public void answerNotFound(){
       Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
       Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
       PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,1);
       Mockito.when(paperAnswerRepository.findById(paperAnswerPrimaryKey)).thenReturn(Optional.empty());

       paperAnswerService.markStudentPaper(1L,1L,1L,1,null);
    }

    @Test(expected = NotFoundException.class)
    public void questionNotFound(){
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,1);
        PaperAnswer paperAnswer = new PaperAnswer();
        Mockito.when(paperAnswerRepository.findById(paperAnswerPrimaryKey)).thenReturn(Optional.of(paperAnswer));
        Mockito.when(questionRepository.findById(3L)).thenReturn(Optional.empty());
        MarkForm markForm = new MarkForm();
        markForm.setQuestionId(3L);

        paperAnswerService.markStudentPaper(1L,1L,1L,1 ,Set.of(markForm));
    }

    @Test(expected = NotFoundException.class)
    public void answerNotFoundInPaper(){
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
        PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,1);
        PaperAnswer paperAnswer = new PaperAnswer();
        Mockito.when(paperAnswerRepository.findById(paperAnswerPrimaryKey)).thenReturn(Optional.of(paperAnswer));
        Answer answer1 = new Answer();
        Question question1 = new Question();
        question1.setId(1L);
        answer1.setAnswerPrimaryKey(new AnswerPrimaryKey(paperAnswer,question1));
        paperAnswer.setAnswers(List.of(answer1));
        Question question3 = new Question();
        question3.setId(3L);
        Mockito.when(questionRepository.findById(3L)).thenReturn(Optional.of(question3));
        MarkForm markForm = new MarkForm();
        markForm.setQuestionId(3L);


        paperAnswerService.markStudentPaper(1L,1L,1L,1, Set.of(markForm));
    }

    @Test
    public void markTest(){
        //initialize question and paper
        Question question2 = new Question();
        question2.setId(2L);
        question2.setQuestionType(QuestionType.SUBJECTIVE);

        Question question3 = new Question();
        question3.setId(3L);
        question3.setQuestionType(QuestionType.SUBJECTIVE);

        Paper paper2 = new Paper();
        paper2.setId(2L);
        paper.setCourse(course);

        PaperWithQuestions paperWithQuestion1 = new PaperWithQuestions(paper2,question2,1,5.0D);
        PaperWithQuestions paperWithQuestion2 = new PaperWithQuestions(paper2,question3,2,5.0D);

        paper2.setQuestions(List.of(paperWithQuestion1,paperWithQuestion2));

        //initialize paper answer
        PaperAnswer paperAnswer1 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey1 = new PaperAnswerPrimaryKey(user,paper2,1);
        paperAnswer1.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey1);
        paperAnswer1.setState(PaperAnswerState.FINISHED);

        PaperAnswer paperAnswer2 = new PaperAnswer();
        PaperAnswerPrimaryKey paperAnswerPrimaryKey2 = new PaperAnswerPrimaryKey(user,paper2,2);
        paperAnswer2.setPaperAnswerPrimaryKey(paperAnswerPrimaryKey2);
        paperAnswer2.setState(PaperAnswerState.FINISHED);

        Answer answer1 = new Answer();
        AnswerPrimaryKey answerPrimaryKey1 = new AnswerPrimaryKey(paperAnswer1,question2);
        answer1.setAnswerPrimaryKey(answerPrimaryKey1);
        answer1.setAnswer("111");
        Answer answer2 = new Answer();
        AnswerPrimaryKey answerPrimaryKey2 = new AnswerPrimaryKey(paperAnswer1,question3);
        answer2.setAnswerPrimaryKey(answerPrimaryKey2);
        answer2.setAnswer("222");

        Answer answer3 = new Answer();
        AnswerPrimaryKey answerPrimaryKey3 = new AnswerPrimaryKey(paperAnswer2,question2);
        answer3.setAnswerPrimaryKey(answerPrimaryKey3);
        answer3.setAnswer("333");
        Answer answer4 = new Answer();
        AnswerPrimaryKey answerPrimaryKey4 = new AnswerPrimaryKey(paperAnswer2,question3);
        answer4.setAnswerPrimaryKey(answerPrimaryKey4);
        answer4.setAnswer("444");

        paperAnswer1.setAnswers(List.of(answer1,answer2));
        paperAnswer2.setAnswers(List.of(answer3,answer4));

        //initialize mark form

        MarkForm markForm1 = new MarkForm("good",2L,5.0D);
        MarkForm markForm2 = new MarkForm("bad",3L,0D);
        MarkForm markForm3 = new MarkForm("need to improve",2L,2.5D);
        MarkForm markForm4 = new MarkForm("little problem",3L,4.00D);

        //Mockito get info
        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
        Mockito.when(paperService.getPaperInfo(2L,1L)).thenReturn(paper2);

        Mockito.when(paperAnswerRepository.findById(paperAnswerPrimaryKey1))
                .thenReturn(Optional.of(paperAnswer1));
        Mockito.when(paperAnswerRepository.findById(paperAnswerPrimaryKey2))
                .thenReturn(Optional.of(paperAnswer2));

        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));
        Mockito.when(questionRepository.findById(3L)).thenReturn(Optional.of(question3));

        Mockito.when(answerRepository.findById(answerPrimaryKey1)).thenReturn(Optional.of(answer1));
        Mockito.when(answerRepository.findById(answerPrimaryKey2)).thenReturn(Optional.of(answer2));
        Mockito.when(answerRepository.findById(answerPrimaryKey3)).thenReturn(Optional.of(answer3));
        Mockito.when(answerRepository.findById(answerPrimaryKey4)).thenReturn(Optional.of(answer4));

        Mockito.when(paperWithQuestionsRepository.getOne(new PaperWithQuestionsPrimaryKey(paper2,question2))).thenReturn(paperWithQuestion1);
        Mockito.when(paperWithQuestionsRepository.getOne(new PaperWithQuestionsPrimaryKey(paper2,question3))).thenReturn(paperWithQuestion2);

        Mockito.when(paperAnswerRepository.save(any(PaperAnswer.class))).thenAnswer(i -> i.getArguments()[0]);

        paperAnswer1 = paperAnswerService.markStudentPaper(1L,1L,2L,1,Set.of(markForm1,markForm2));
        paperAnswer2 = paperAnswerService.markStudentPaper(1L,1L,2L,2,Set.of(markForm3,markForm4));

        //assert grade
        assertThat(paperAnswer1.getGrade()).isEqualTo(5D);
        assertThat(paperAnswer2.getGrade()).isEqualTo(6.5D);

        //assert comment
        assertThat(paperAnswer1.getAnswers().get(0).getComment()).isEqualTo("good");
        assertThat(paperAnswer2.getAnswers().get(1).getComment()).isEqualTo("little problem");

    }

}
