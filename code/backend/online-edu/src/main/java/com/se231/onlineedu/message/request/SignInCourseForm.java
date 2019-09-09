package com.se231.onlineedu.message.request;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author liu
 */
public class SignInCourseForm {
    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;



    private Double longitude;

    private Double latitude;

    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;

    private int signInNo;

    public int getSignInNo() {
        return signInNo;
    }

    public void setSignInNo(int signInNo) {
        this.signInNo = signInNo;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
