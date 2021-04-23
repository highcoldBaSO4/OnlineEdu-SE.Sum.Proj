package com.se231.onlineedu.repository;

import java.util.List;
import java.util.Optional;
import com.se231.onlineedu.model.Paper;
import com.se231.onlineedu.model.PaperAnswer;
import com.se231.onlineedu.model.PaperAnswerPrimaryKey;
import com.se231.onlineedu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Paper Answer Repository
 *
 * used to operate with database
 *
 * @author Zhe Li
 *
 * @date 2019/07/10
 */
public interface PaperAnswerRepository extends JpaRepository<PaperAnswer, PaperAnswerPrimaryKey> {
    /**
     * this function helps to get the max answer times of a user and a paper.
     * @param user_id   id of user
     * @param paper_id  id of paper
     * @return  times of answer.
     */
    @Query(value = "select max(times) from paper_answer where user_id = ?1 and paper_id=?2 ",nativeQuery = true)
    Optional<Integer> getMaxTimes(Long user_id,Long paper_id);

    /**
     * get list of all paper answer of a specific paper
     * @param paperId   id of the paper
     * @return
     */
    @Query(value = "select * from paper_answer where paper_id = ?1",nativeQuery = true)
    List<PaperAnswer> getPaperAnswers(Long paperId);

    /**
     * get a student's answer list
     * @param paperId   id of paper
     * @param userId    id of user
     * @return  user's answer list
     */
    @Query(value = "select * from paper_answer where paper_id = ?1 and user_id = ?2",nativeQuery = true)
    List<PaperAnswer> getPersonalPaperAnswer(Long paperId, Long userId);

    List<PaperAnswer> findAllByPaperAnswerPrimaryKey_PaperAndAndPaperAnswerPrimaryKey_User(Paper paper, User user);
}
