package com.se231.onlineedu.service;

import java.util.List;
import java.util.Set;
import com.se231.onlineedu.message.request.MarkForm;
import com.se231.onlineedu.message.request.SubmitAnswerForm;
import com.se231.onlineedu.model.Answer;
import com.se231.onlineedu.model.PaperAnswer;
import com.se231.onlineedu.model.PaperAnswerState;
import org.springframework.web.multipart.MultipartFile;

/**
 * Paper Answer Service Interface
 *
 * interface of service related to students answer papers.
 *
 * @author Zhe Li
 *
 * @date 2019/07/10
 */
public interface PaperAnswerService {
    /**
     * this service allow students to answer a paper ,then record their answer and mark a score.
     * @param userId    id of user who answer the paper
     * @param courseId  id of the course
     * @param paperId   id of the answered paper
     * @param form   a form contains answer and state.
     * @return  the paper with answers the user just submit.
     * @throws Exception    mainly not found exception.
     */
    PaperAnswer submitAnswer(Long userId, Long courseId, Long paperId, SubmitAnswerForm form)throws Exception;

    /**
     * this service will mark all answer of a specific paper.
     * @param paperId
     */
    void autoMark(Long paperId);

    /**
     * get a student's answer list
     * @param paperId   id of paper
     * @param userId    id of user
     * @return  user's answer list
     */
    List<PaperAnswer> getPersonalPaperAnswer(Long paperId,Long userId);

    /**
     * this service allow teacher to mark students' paper
     * @param studentId id of student
     * @param paperId   id of paper
     * @param times times of the answer
     * @param markForms marking forms
     * @param courseId id of course
     * @return  the marked paper answer
     */
    PaperAnswer markStudentPaper(Long courseId, Long studentId, Long paperId, Integer times, Set<MarkForm> markForms);

    /**
     * this service allow students to submit subjective question
     * @param courseId  id of course
     * @param userId    id of student
     * @param paperId   id of paper
     * @param questionId id of question
     * @param images    images of answer
     * @param answerText text of answer
     * @param state state of answer
     * @param file file of answer
     * @return  the answered paper
     */
    PaperAnswer submitSubjectiveQuestion(Long courseId, Long userId, Long paperId, Long questionId,
                                         String answerText, MultipartFile[] images,MultipartFile[] file, PaperAnswerState state);

    /**
     * this service allow users to change the state of his last paper answer
     * @param courseId  id of course
     * @param userId    id of user
     * @param paperId   id of paper
     * @param state the updated state
     * @return  the new paper answer
     */
    PaperAnswer changePaperAnswerState(Long courseId, Long userId, Long paperId,PaperAnswerState state);

    List<PaperAnswer> getStudentAnswer(Long courseId, Long paperId, Long studentId);
}
