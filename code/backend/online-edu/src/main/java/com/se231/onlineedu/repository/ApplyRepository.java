package com.se231.onlineedu.repository;

import java.util.List;
import com.se231.onlineedu.model.Apply;
import com.se231.onlineedu.model.ApplyPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Teacher Apply For Course Entity repository
 *
 * used to operate with database about the table TeacherApplyFotCourse
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */
public interface ApplyRepository extends JpaRepository<Apply, ApplyPrimaryKey> {
    /**
     * get all applies for a specific course prototype
     * @param course_prototype_id   id of the course prototype
     * @return  list of apply.
     */
    @Query(value = "select * from teacher_apply_for_course  where course_prototype_id = ?1",nativeQuery = true)
    List<Apply> findAppliesByPrototypeId(Long course_prototype_id);
}
