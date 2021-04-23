package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Question Repository Interface
 *
 * the interface used to operate with database
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
public interface QuestionRepository extends JpaRepository<Question,Long> {

}
