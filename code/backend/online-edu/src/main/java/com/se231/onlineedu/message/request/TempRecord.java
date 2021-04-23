package com.se231.onlineedu.message.request;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Zhe Li
 * @date 2019/09/06
 */
public class TempRecord {
    private String action;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
