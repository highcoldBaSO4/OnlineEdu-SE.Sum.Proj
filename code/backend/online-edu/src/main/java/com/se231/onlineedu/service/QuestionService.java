package com.se231.onlineedu.service;

import com.se231.onlineedu.model.Question;
import com.se231.onlineedu.model.QuestionType;

import java.util.List;

/**
 * Service Interface related to questions
 *
 * this interface contains all the service related to a specific question.
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
public interface QuestionService {

    /**
     * this service allow the user of the prototype
     * @param coursePrototypeId the id of uploaded prototype
     * @param questionType  type of the question
     * @param questionText  the text of question
     * @param answer    the answer of question
     * @return  the generated question
     * @throws Exception    mainly contains not found exception
     */
    Question submitQuestion(Long coursePrototypeId, QuestionType questionType,
                            String questionText,String answer);

    /**
     * get info question
     * @param id
     * @return
     */
    Question getQuestionInfo(Long id);

    /**
     * save images
     * @param questionId
     * @param urls
     * @return
     */
    Question saveImages(Long questionId, List<String> urls);
}
