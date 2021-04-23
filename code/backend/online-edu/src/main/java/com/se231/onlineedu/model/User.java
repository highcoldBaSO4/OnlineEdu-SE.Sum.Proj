package com.se231.onlineedu.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se231.onlineedu.message.request.SignUpForm;
import com.se231.onlineedu.message.request.UserExcel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * User class
 *
 * User class is the main entity used to manage user info and control authorization
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        })
})
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private Long tel;

    private String university;

    private String major;

    private int grade;

    private String sno;

    private String tno;

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    private String realName;

    private String sex;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    private String avatarUrl;

    @ManyToMany
    @JsonBackReference
    private List<SignIn> signIns = new ArrayList<>();

    public List<SignIn> getSignIns() {
        return signIns;
    }

    public void setSignIns(List<SignIn> signIns) {
        this.signIns = signIns;
    }

    @Column(name = "enabled")
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User(){
        super();
        this.enabled = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(Long id, @NotBlank String username, @NotBlank String password, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "assist_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))
    private List<Course> assistCourses = new ArrayList<>();

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teacher")
    private List<Course> teachCourses = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "learnPrimaryKey.student")
    private List<Learn> learns = new ArrayList<>();

    public List<Learn> getLearns() {
        return learns;
    }

    public void setLearns(List<Learn> learns) {
        this.learns = learns;
    }

    @Transient
    @JsonIgnore
    public List<Course> getLearnCourses(){
        List<Course> courses = new ArrayList<>();
        for(Learn learn: getLearns()){
            courses.add(learn.getLearnPrimaryKey().getCourse());
        }
        return courses;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public User(@NotBlank String username, @NotBlank String password) {
        this.username = username;
        this.password = password;
    }

    public User(SignUpForm form) {
        this.username = form.getUsername();
        this.email = form.getEmail();
        this.tel = Long.parseLong(form.getTel());
        this.university = form.getUniversity();
        this.major = form.getMajor();
        this.grade = form.getGrade();
        this.sno = form.getSno();
        this.realName = form.getRealName();
        this.sex = form.getSex();
    }

    public User(UserExcel userExcel){
        this.username = userExcel.getUsername();
        this.email = userExcel.getEmail();
        this.tel = userExcel.getTel();
        this.university = userExcel.getUniversity();
        this.major = userExcel.getMajor();
        this.grade = userExcel.getGrade();
        this.sno = userExcel.getSno();
        this.realName = userExcel.getRealName();
        this.sex = userExcel.getSex();
        this.enabled=true;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Course> getAssistCourses() {
        return assistCourses;
    }

    public void setAssistCourses(List<Course> assistCourses) {
        this.assistCourses = assistCourses;
    }

    public List<Course> getTeachCourses() {
        return teachCourses;
    }

    public void setTeachCourses(List<Course> teachCourses) {
        this.teachCourses = teachCourses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
