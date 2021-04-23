package com.se231.onlineedu.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.model.CoursePrototype;
import com.se231.onlineedu.model.Question;
import com.se231.onlineedu.model.QuestionType;
import com.se231.onlineedu.repository.QuestionRepository;
import com.se231.onlineedu.service.CoursePrototypeService;
import com.se231.onlineedu.service.QuestionService;
import com.se231.onlineedu.serviceimpl.QuestionServiceImpl;
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
 * @date 2019/08/01
 */
@RunWith(SpringRunner.class)
public class QuestionServiceImplTest {
    @TestConfiguration
    static class QuestionServiceImplTestContextConfig{
        @Bean
        public QuestionService questionService(){
            return new QuestionServiceImpl();
        }
    }

    @Autowired
    QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private CoursePrototypeService coursePrototypeService;

    @Test(expected = NotFoundException.class)
    public void questionNotFoundTest() {
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        questionService.getQuestionInfo(1L);
    }

    @Test
    public void getQuestionTest(){
        Question question = new Question();
        question.setId(1L);
        question.setQuestion("test");
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Question question1 = questionService.getQuestionInfo(1L);

        assertThat(question1.getId()).isEqualTo(1L);
        assertThat(question1.getQuestion()).isEqualTo("test");
    }

    @Test
    public void submitQuestionTest(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);

        String questionText = "test question\0\rtestA\0\rtestB\0\r";
        String answer = "answer";

        Mockito.when(coursePrototypeService.getCoursePrototypeInfo(1L)).thenReturn(coursePrototype);
        Mockito.when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        Question question = questionService.submitQuestion(1L, QuestionType.SINGLE_ANSWER,questionText,answer);

        assertThat(question.getQuestion()).isEqualTo(questionText);
        assertThat(question.getOptions().get('A')).isEqualTo("testA");
        assertThat(question.getContent()).isEqualTo("test question");
        assertThat(question.getAnswer()).isEqualTo(answer);
    }
}
