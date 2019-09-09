package com.se231.onlineedu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Course Class
 * <p>
 * Course class is the main class to manage course information.
 *
 * @author zhe li
 * @date 2019/7/1
 */
@ApiModel(value = "课程，即基于课程原型衍生的实际的课程，有老师有学生")
@Entity
@Table(name = "Courses")
public class Course {
    @ApiModelProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatarUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期", required = true)
    @NotNull
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期", required = true)
    @NotNull
    private Date endDate;

    @ApiModelProperty(value = "课程的状态", example = "有以下几个状态： APPLYING,READY_TO_START,TEACHING,FINISHED,NOT_PASS")
    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseState state;

    @ApiModelProperty(value = "该课程基于的课程原型")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private CoursePrototype coursePrototype;

    @ApiModelProperty("课程名称")
    private String courseTitle;

    @ApiModelProperty("地点")
    private String location;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="course_time_slots",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "time_slot_id"))
    @ApiModelProperty("上课时间段")
    private List<TimeSlot> timeSlots = new ArrayList<>();


    @ApiModelProperty("该课程的助教")
    @JsonManagedReference
    @ManyToMany(mappedBy = "assistCourses")
    private List<User> teacherAssistants = new ArrayList<>();

    @JsonManagedReference(value = "learn")
    @OneToMany(mappedBy = "learnPrimaryKey.course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Learn> learns = new ArrayList<>();

    @JsonManagedReference
    @ApiModelProperty("该课程的老师")
    @ManyToOne()
    @JoinTable(name = "teach_course",
            joinColumns = @JoinColumn(name = "courses_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User teacher;

    @ApiModelProperty("该课程的所有试卷")
    @OneToMany(mappedBy = "course")
    private List<Paper> papers = new ArrayList<>();

    @JsonManagedReference
    @ApiModelProperty("该课程的所有签到")
    @OneToMany(mappedBy = "signInPrimaryKey.course")
    private List<SignIn> signIns = new ArrayList<>();

    @OneToMany(mappedBy = "sectionPrimaryKey.course")
    private List<Section> sectionList;

    @ApiModelProperty("该课程的公告集合")
    @OneToMany(mappedBy = "noticePrimaryKey.course")
    private List<Notice> notices = new ArrayList<>();


    public Course() {
        sectionList = new ArrayList<>();
        timeSlots = new ArrayList<>();
        learns = new ArrayList<>();
        teacherAssistants = new ArrayList<>();
        notices = new ArrayList<>();
        signIns = new ArrayList<>();
        papers = new ArrayList<>();
        notices = new ArrayList<>();
    }

    public List<SignIn> getSignIns() {
        return signIns;
    }

    public void setSignIns(List<SignIn> signIns) {
        this.signIns = signIns;
    }

    @Transient
    @JsonManagedReference
    public List<User> getStudents() {
        List<User> students = new ArrayList<>();
        for (Learn learn : getLearns()) {
            students.add(learn.getLearnPrimaryKey().getStudent());
        }
        return students;
    }

    public Course(@NotNull Date startDate, @NotNull Date endDate, @NotNull CourseState state, CoursePrototype coursePrototype, User teacher) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.coursePrototype = coursePrototype;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CourseState getState() {
        return state;
    }

    public void setState(CourseState state) {
        this.state = state;
    }

    public CoursePrototype getCoursePrototype() {
        return coursePrototype;
    }

    public void setCoursePrototype(CoursePrototype coursePrototype) {
        this.coursePrototype = coursePrototype;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<User> getTeacherAssistants() {
        return teacherAssistants;
    }

    public void setTeacherAssistants(List<User> teacherAssistants) {
        this.teacherAssistants = teacherAssistants;
    }

    public List<Learn> getLearns() {
        return learns;
    }

    public void setLearns(List<Learn> learns) {
        this.learns = learns;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
