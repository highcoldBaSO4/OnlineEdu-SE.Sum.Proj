package com.se231.onlineedu.serviceimpl;

import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.model.CoursePrototype;
import com.se231.onlineedu.model.Question;
import com.se231.onlineedu.model.QuestionType;
import com.se231.onlineedu.repository.QuestionRepository;
import com.se231.onlineedu.service.CoursePrototypeService;
import com.se231.onlineedu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Question Service Interface Implementation Class
 *
 * contains main service logic related to question.
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CoursePrototypeService coursePrototypeService;

    @Override
    public Question submitQuestion(Long coursePrototypeId, QuestionType questionType,
                            String questionText, String answer) {
        CoursePrototype coursePrototype = coursePrototypeService.getCoursePrototypeInfo(coursePrototypeId);
        Question question=new Question();
        question.setQuestion(questionText);
        question.setAnswer(answer);
        question.setCoursePrototype(coursePrototype);
        question.setQuestionType(questionType);
        return questionRepository.save(question);
    }

    @Override
    public Question getQuestionInfo(Long id){
        return questionRepository.findById(id).orElseThrow(()-> new NotFoundException("该问题不存在"));
    }

    @Override
    public Question saveImages(Long questionId, List<String> urls){
        Question question = getQuestionInfo(questionId);
        question.setImages(urls);
        return questionRepository.save(question);
    }
}
