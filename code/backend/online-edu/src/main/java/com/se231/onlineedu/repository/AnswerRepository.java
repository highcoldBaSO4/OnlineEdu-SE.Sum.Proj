package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.Answer;
import com.se231.onlineedu.model.AnswerPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Answer Repository Interface
 *
 * used to operate with database
 *
 * @author Zhe Li
 * @date 2019/07/10
 */
public interface AnswerRepository extends JpaRepository<Answer, AnswerPrimaryKey> {
}
