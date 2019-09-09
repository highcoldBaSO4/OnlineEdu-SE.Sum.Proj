package com.se231.onlineedu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liu
 */
@Document
public class Forum {

    @Id
    private String id;

    private String title;

    private boolean isLocked = false;

    @NotBlank
    private String content;

    @NotBlank
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt = new Date();

    @NotBlank
    private Long courseId;

    @NotBlank
    private int secNo;

    private int likes = 0;

    private String path;

    private List<String> imageUrls = new ArrayList<>();

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public int getSecNo() {
        return secNo;
    }

    public void setSecNo(int secNo) {
        this.secNo = secNo;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPath() {
        DateFormat dateFormat =new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss");
        String strPostedAt = dateFormat.format(createdAt);
        return (path == null? "" : path) + "/" + strPostedAt + ":" + id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Forum(String id, String title, String content, @NotBlank Long userId, Date createdAt, @NotBlank Long courseId, @NotBlank int secNo, int likes, String path, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.courseId = courseId;
        this.secNo = secNo;
        this.likes = likes;
        this.path = path;
        this.imageUrls = imageUrls;
    }

    public Forum() {
    }
}
