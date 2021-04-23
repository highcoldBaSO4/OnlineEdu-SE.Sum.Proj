package com.se231.onlineedu.message.response;

import com.se231.onlineedu.model.User;

import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Personal information form class
 *
 * this form is used to allow user to check and modify their personal information.
 *
 * @author Zhe Li
 *
 * @date 2019/07/08
 */
public class PersonalInfo {
    @Pattern(regexp = "0?(13|14|15|18|17)[0-9]{9}")
    private String tel;

    private String university;

    private String major;

    private int grade;

    private String sno;

    private String realName;

    private String sex;

    public PersonalInfo(User user) {
        this.grade=user.getGrade();
        this.major=user.getMajor();
        this.realName=user.getRealName();
        this.sex=user.getSex();
        this.sno=user.getSno();
        if(user.getTel()!=null) {
            this.tel = user.getTel().toString();
        }
        this.university=user.getUniversity();
    }

    public PersonalInfo() {
    }

    public void modifyUserInfo(User user){
        user.setGrade(grade);
        user.setMajor(major);
        user.setRealName(realName);
        user.setSex(sex);
        user.setSno(sno);
        user.setUniversity(university);
        user.setTel(Long.parseLong(tel));
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
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
        PersonalInfo that = (PersonalInfo) o;
        return grade == that.grade &&
                Objects.equals(tel, that.tel) &&
                Objects.equals(university, that.university) &&
                Objects.equals(major, that.major) &&
                Objects.equals(sno, that.sno) &&
                Objects.equals(realName, that.realName) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash( tel, university, major, grade, sno, realName, sex);
    }
}
