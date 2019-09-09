package com.se231.onlineedu.model;

import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class VerificationToken {

    private static int EXPIRATION=20;

    private String token;

    private int times;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }


    private Date expiryDate;

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }


    public VerificationToken() {
        Integer tokenInt = ThreadLocalRandom.current().nextInt(100000, 1000000);
        token = tokenInt.toString();
        expiryDate = calculateExpiryDate(EXPIRATION);
        System.out.println(EXPIRATION);
        this.times = 0;
    }

    public VerificationToken(String token) {
        this.token = token;
        expiryDate = calculateExpiryDate(EXPIRATION);
        this.times = 0;
    }

    public VerificationToken(String token, Date expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
        times=0;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
