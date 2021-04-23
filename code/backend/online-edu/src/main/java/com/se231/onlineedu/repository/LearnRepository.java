package com.se231.onlineedu.repository;

import java.util.List;
import com.se231.onlineedu.model.Learn;
import com.se231.onlineedu.model.LearnPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Learn Repository Interface
 *
 * used to operate with db.
 *
 * @author Zhe Li
 * @date 2019/07/16
 */
public interface LearnRepository extends JpaRepository<Learn, LearnPrimaryKey> {
    Optional<Learn> findByLearnPrimaryKey_Student_IdAndLearnPrimaryKey_Course_Id(Long userId, Long courseId);
}
