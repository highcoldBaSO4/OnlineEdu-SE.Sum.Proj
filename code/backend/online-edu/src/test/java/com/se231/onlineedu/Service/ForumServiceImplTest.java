package com.se231.onlineedu.Service;

import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.model.Forum;
import com.se231.onlineedu.repository.ForumRepository;
import com.se231.onlineedu.service.ForumService;
import com.se231.onlineedu.serviceimpl.ForumServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
public class ForumServiceImplTest {
    @TestConfiguration
    static class ForumServiceImplTestConfig{
        @Bean
        public ForumService forumService(){
            return new ForumServiceImpl();
        }
    }

    @Autowired
    private ForumService forumService;

    @MockBean
    private ForumRepository forumRepository;

    @Before
    public void setup(){
        Mockito.when(forumRepository.save(any(Forum.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void getForumsBySec(){
        Forum forum = new Forum();
        forum.setUserId(1L);
        forum.setSecNo(1);
        forum.setUserId(1L);
        forum.setId("uftf");

        List<Forum> forums = new ArrayList<>();
        forums.add(forum);

        Mockito.when(forumRepository.findByCourseIdAndSecNo(1L,1)).thenReturn(forums);
        List<Forum> founds = forumService.getForumsBySection(1L,1);
        assertThat(founds.size()).isEqualTo(1);
        assertThat(founds.get(0).getId()).isEqualTo("uftf");
    }

    @Test
    public void getForumByCourse(){
        Forum forum = new Forum();
        forum.setUserId(1L);
        forum.setSecNo(1);
        forum.setUserId(1L);
        forum.setId("uftf");

        List<Forum> forums = new ArrayList<>();
        forums.add(forum);

        Mockito.when(forumRepository.findByCourseId(1L)).thenReturn(forums);
        List<Forum> founds = forumService.getForumsByCourse(1L);
        assertThat(founds.size()).isEqualTo(1);
        assertThat(founds.get(0).getId()).isEqualTo("uftf");
    }

    @Test(expected = NotFoundException.class)
    public void getEmptyForum(){
        Mockito.when(forumRepository.findById(anyString())).thenReturn(Optional.empty());
        forumService.getForum("adgv");
    }

    @Test
    public void getForumById(){
        Forum forum = new Forum();
        forum.setUserId(1L);
        forum.setSecNo(1);
        forum.setUserId(1L);
        forum.setId("uftf");

        Optional<Forum> forumOptional = Optional.of(forum);
        Mockito.when(forumRepository.findById("uftf")).thenReturn(forumOptional);
        Forum found = forumService.getForum("uftf");
        assertThat(found.getUserId()).isEqualTo(1L);
        assertThat(found.getSecNo()).isEqualTo(1);
    }

    @Test
    public void updateForum(){
        Forum forum = new Forum();
        forum.setUserId(1L);
        forum.setSecNo(1);
        forum.setUserId(1L);
        forum.setId("uftf");

        Forum found = forumService.updateForum(forum);
        assertThat(found.getId()).isEqualTo("uftf");
    }



}
