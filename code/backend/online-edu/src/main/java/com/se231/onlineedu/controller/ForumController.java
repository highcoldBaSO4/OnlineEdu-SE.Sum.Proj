package com.se231.onlineedu.controller;


import com.se231.onlineedu.model.Course;
import com.se231.onlineedu.model.Forum;
import com.se231.onlineedu.security.services.UserPrinciple;
import com.se231.onlineedu.service.*;
import com.se231.onlineedu.util.SaveFileUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

/**
 * @author liu
 * @date 2017/07/11
 */
@RestController
@RequestMapping("/api")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @Autowired
    private DiscernSensitiveWordsService discernSensitiveWordsService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private WordCloudService wordCloudService;

    @Autowired
    private LearnService learnService;

    private int limit = 5120000;


    @ApiOperation("上传论坛内容")
    @PostMapping("/courses/{courseId}/sections/{secNo}/forums")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TEACHING_ADMIN','SUPER_ADMIN')")
    public Forum updateForum(@RequestBody Forum forum, @PathVariable Long courseId, @PathVariable int secNo, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        if(!discernSensitiveWordsService.discern(forum.getContent())){
            for(String email: courseService.getTeacherAssistantAndTeacherEmail(courseId)){
                Course course = courseService.getCourseInfo(courseId);
                emailSenderService.sendSensitiveWordsDetectedWords(email, course.getCourseTitle(), course.getSectionList().get(secNo).getTitle(), userPrinciple.getUsername());
            }
        }
        forum.setCourseId(courseId);
        forum.setSecNo(secNo);
        forum.setUserId(userPrinciple.getId());
        return forumService.updateForum(forum);
    }

    @ApiOperation("获取课程下的所有论坛")
    @GetMapping("/courses/{courseId}/forums")
    public List<Forum> getForumsByCourse(@PathVariable Long courseId){
        return forumService.getForumsByCourse(courseId);
    }


    @ApiOperation("获取课程section下的的所有论坛")
    @GetMapping("/courses/{courseId}/sections/{secNo}/forums")
    public List<Forum> getForums(@PathVariable Long courseId, @PathVariable int secNo){
        return forumService.getForumsBySection(courseId, secNo);
    }


    @ApiOperation("封锁论坛")
    @DeleteMapping("/forums/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TEACHING_ADMIN','SUPER_ADMIN')")
    public Forum deleteForum(@PathVariable String id){
        Forum forum = forumService.getForum(id);
        forum.setContent("该内容已被锁");
        forum.setTitle("该内容已被锁");
        forum.setImageUrls(new ArrayList<>());
        forum.setLocked(true);
        return forumService.updateForum(forum);
    }


    @ApiOperation("获forum用id")
    @GetMapping("/forums/{id}")
    public Forum getForumById(@PathVariable String id) {
        return forumService.getForum(id);
    }



    @ApiOperation("为某个论坛上传图片")
    @PostMapping("/forums/{id}/images")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TEACHING_ADMIN','SUPER_ADMIN')")
    public Forum insertImages(@PathVariable String id, @RequestParam("images") MultipartFile[] multipartFiles) throws IOException {
        Forum forum = forumService.getForum(id);
        List<String> urls = SaveFileUtil.saveImages(multipartFiles, limit);
        forum.setImageUrls(urls);
        return forumService.updateForum(forum);
    }

    @ApiOperation("删除某个图片")
    @DeleteMapping("/forums/{id}/images/{filename}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'TEACHING_ADMIN','SUPER_ADMIN')")
    public boolean deleteImage(@PathVariable String id, @PathVariable String filename) throws IOException {
        Forum forum = forumService.getForum(id);
        forum.getImageUrls().remove(filename);
        forumService.updateForum(forum);
        return SaveFileUtil.deleteImage(filename);
    }

    @ApiOperation("生成词云图")
    @PostMapping("/users/{userId}/courses/{courseId}/forums/")
    public String wordCloud(@PathVariable Long userId, @PathVariable Long courseId) throws IOException {
        List<String> strings = new ArrayList<>();
        for(Forum forum: forumService.getForumByUserAndCourse(userId, courseId)){
            if(forum.isLocked()){
                continue;
            }
            if(forum.getTitle() != null){
                strings.add(forum.getTitle());
            }
            strings.add(forum.getContent());
        }
        System.out.println(strings);
        String url = wordCloudService.generateWordCloud(strings);
        learnService.saveWordCloudUrl(userId,courseId,url);
        return url;
    }


}