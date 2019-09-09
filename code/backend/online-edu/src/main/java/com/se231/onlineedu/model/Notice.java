package com.se231.onlineedu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @author yuxuanLiu
 * @date 2019/07/22
 */
@Entity
public class Notice {
    @EmbeddedId
    @JsonIgnore
    private NoticePrimaryKey noticePrimaryKey;

    public NoticePrimaryKey getNoticePrimaryKey() {
        return noticePrimaryKey;
    }

    public void setNoticePrimaryKey(NoticePrimaryKey noticePrimaryKey) {
        this.noticePrimaryKey = noticePrimaryKey;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date issueDate = new Date();

    private String content;

    private String title;

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Notice() {
    }
}
