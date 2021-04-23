package com.se231.onlineedu.Repository;


import com.se231.onlineedu.model.Forum;
import com.se231.onlineedu.repository.ForumRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author liu
 * @date 2019/07/17
 */
@RunWith(SpringRunner.class)
@DataMongoTest

public class ForumRepositoryTest {


    @Autowired
    private ForumRepository forumRepository;

    @Test
    public void whenFindByCourseId_thenReturnForum(){
        Forum forum = new Forum();
        forum.setUserId(1L);
        forum.setCourseId(1L);
        forum.setSecNo(1);
        forumRepository.save(forum);
        List<Forum> founds = forumRepository.findByCourseId(1L);
        assertThat(founds.get(0).getUserId()).isEqualTo(1L);
    }

    @Test
    public void findByCourseIdAndSecNo(){
        Forum forum = new Forum();
        forum.setUserId(2L);
        forum.setCourseId(2L);
        forum.setSecNo(2);
        forumRepository.save(forum);
        List<Forum> founds = forumRepository.findByCourseIdAndSecNo(2L,2);
        assertThat(founds.get(0).getUserId()).isEqualTo(2L);
    }
}
