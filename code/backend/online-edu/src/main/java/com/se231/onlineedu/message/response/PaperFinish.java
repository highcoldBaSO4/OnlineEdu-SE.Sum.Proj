package com.se231.onlineedu.message.response;

import com.se231.onlineedu.model.Paper;
import com.se231.onlineedu.model.PaperAnswerState;
import com.se231.onlineedu.model.User;

/**
 * Response of paper finish status
 *
 * mark the state of paper finishing.
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */
public class PaperFinish {
    private PaperAnswerState state;

    private Paper paper;

    private int times;

    private User student;

    public PaperFinish() {

    }

    public PaperFinish(Paper paper, PaperAnswerState state, int times){
        this.paper=paper;
        this.state=state;
        this.times=times;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public PaperAnswerState getState() {
        return state;
    }

    public void setState(PaperAnswerState state) {
        this.state = state;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
