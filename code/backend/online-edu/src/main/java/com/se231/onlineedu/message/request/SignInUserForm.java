package com.se231.onlineedu.message.request;

/**
 * @author liu
 */
public class SignInUserForm {
    private Long courseId;

    private int signInNo;

    private Double longitude;

    private Double latitude;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public int getSignInNo() {
        return signInNo;
    }

    public void setSignInNo(int signInNo) {
        this.signInNo = signInNo;
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
