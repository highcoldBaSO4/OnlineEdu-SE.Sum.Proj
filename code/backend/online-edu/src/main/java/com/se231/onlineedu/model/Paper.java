package com.se231.onlineedu.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Paper Entity Class
 *
 * paper is a list of questions which generate a test or a exercise.
 *
 * @author Zhe Li
 *
 * @date 2019/7/10
 */
@ApiModel("试卷,作业的实体类")
@Entity
public class Paper {
    @ApiModelProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date start;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date end;

    @ApiModelProperty("试卷的习题集")
    @OneToMany
    private List<PaperWithQuestions> questions;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ApiModelProperty("作业名称")
    private String title;

    @ApiModelProperty("作业描述")
    private String description;

    public Paper(Date start, Date end, List<PaperWithQuestions> questions) {
        this.start = start;
        this.end = end;
        this.questions = questions;
    }

    public Paper(Date start) {
        this.start = start;
    }

    public Paper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<PaperWithQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<PaperWithQuestions> questions) {
        this.questions = questions;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    @Transient
    public List<Question> getQuestionList(){
        List<Question> questionList = new ArrayList<>();
        for(PaperWithQuestions paperWithQuestions:questions){
            questionList.add(paperWithQuestions.getPaperWithQuestionsPrimaryKey().getQuestion());
        }
        return questionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paper paper = (Paper) o;
        return id.equals(paper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
