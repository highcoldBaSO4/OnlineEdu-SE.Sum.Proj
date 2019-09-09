package com.se231.onlineedu.message.request;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @author Zhe Li
 * @date 2019/08/02
 */
public class GradeExcel extends BaseRowModel {
    @ExcelProperty(index = 0)
    private String sno;

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2)
    private String university;

    @ExcelProperty(index = 3)
    private String department;

    @ExcelProperty(index = 4)
    private double grade;

    public GradeExcel() {
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public boolean hasNull(){
        return name == null || sno == null || department == null || university == null ;
    }
}
