package com.se231.onlineedu.service;

import com.se231.onlineedu.model.Forum;

import java.util.List;

/**
 * @author liu
 */
public interface ForumService {
    /**
     * update forum
     * @param forum
     * @return
     */
    Forum updateForum(Forum forum);

    /**
     * get forum by id
     * @param id
     * @return
     */
    Forum getForum(String id);

    /**
     * get forum by course
     * @param courseId
     * @return
     */
    List<Forum> getForumsByCourse(Long courseId);

    /**
     * get forum by username
     * @param courseId
     * @param secNo
     * @return
     */
    List<Forum> getForumsBySection(Long courseId, int secNo);


    /**
     * get forum by user and course
     * @param userId
     * @param courseId
     * @return
     */
    List<Forum> getForumByUserAndCourse(Long userId, Long courseId);
}
