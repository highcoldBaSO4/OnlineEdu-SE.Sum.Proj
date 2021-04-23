package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.Forum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Forum repository
 *
 * @date 2019/07/11
 *
 * @author YuxuanLiu
 */
@Repository
public interface ForumRepository extends CrudRepository<Forum, String> {
    /**
     * get list of forums by course and section
     * @param courseId course Id
     * @param secNo sec No
     * @return list of forum
     */
    List<Forum> findByCourseIdAndSecNo(Long courseId, int secNo);


    /**
     * get list of forums by courses
     * @param courseId
     * @return list of forum
     */
    List<Forum> findByCourseId(Long courseId);

    /**
     * get list of forums by user id
     * @param userId, courseId
     * @return
     */
    List<Forum> findByUserIdAndCourseId(Long userId, Long courseId);
}
