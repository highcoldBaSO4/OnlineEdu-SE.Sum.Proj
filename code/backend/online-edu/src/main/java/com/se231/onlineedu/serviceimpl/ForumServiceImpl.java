package com.se231.onlineedu.serviceimpl;

import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.model.Forum;
import com.se231.onlineedu.repository.ForumRepository;
import com.se231.onlineedu.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liu
 * @date 2019/07/11
 */
@Service
public class ForumServiceImpl implements ForumService {
    @Autowired
    private ForumRepository forumRepository;

    @Override
    public List<Forum> getForumsBySection(Long courseId, int secNo){
        return forumRepository.findByCourseIdAndSecNo(courseId, secNo);
    }

    @Override
    public List<Forum> getForumsByCourse(Long courseId){
        return forumRepository.findByCourseId(courseId);
    }

    @Override
    public  Forum updateForum(Forum forum){
        return forumRepository.save(forum);
    }

    @Override
    public Forum getForum(String id){
        return forumRepository.findById(id).orElseThrow(() -> new NotFoundException("该论坛不存在"));
    }

    @Override
    public List<Forum> getForumByUserAndCourse(Long userId, Long courseId){
        return forumRepository.findByUserIdAndCourseId(userId, courseId);
    }
}
