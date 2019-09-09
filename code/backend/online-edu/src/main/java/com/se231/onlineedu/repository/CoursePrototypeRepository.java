package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.CoursePrototype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repository of Course prototype
 *
 * used to operate to database
 *
 * @author Zhe Li
 * @date 2019/7/3
 */
@Repository
public interface CoursePrototypeRepository extends JpaRepository<CoursePrototype,Long> {
}
