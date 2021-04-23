package com.se231.onlineedu.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * CoursePrototype Class.
 *
 * Prototype of course,store the resource of a course.
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */
@ApiModel(value = "课程原型的实体类")
@Entity
@Table(name = "coursePrototype")
public class CoursePrototype {
    @ApiModelProperty(dataType = "Long",value = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "名称")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "详情")
    private String description;

    @ApiModelProperty("该课程原型题库中的所有题目")
    @OneToMany(mappedBy = "coursePrototype")
    @JsonManagedReference
    private List<Question> questions;

    @ApiModelProperty("该课程原型的所有衍生课程")
    @OneToMany(mappedBy = "coursePrototype")
    @JsonBackReference
    private List<Course> courses;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CoursePrototypeState state;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> users;

    @ApiModelProperty("该课程原型资源库里的所有资源")
    @OneToMany
    private List<Resource> resources = new ArrayList<>();

    public CoursePrototype() {}

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public CoursePrototype(@NotBlank String title, String description, List<Question> questions, List<Course> courses, @NotBlank CoursePrototypeState state) {
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.courses = courses;
        this.state = state;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUser(List<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public CoursePrototypeState getState() {
        return state;
    }

    public void setState(CoursePrototypeState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursePrototype that = (CoursePrototype) o;
        return id.equals(that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(courses, that.courses) &&
                state == that.state &&
                Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, questions, courses, state, users);
    }
}
