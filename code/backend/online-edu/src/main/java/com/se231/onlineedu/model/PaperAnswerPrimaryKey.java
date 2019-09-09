package com.se231.onlineedu.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * Paper Answer Primary Key Class
 *
 * an answer for paper must be identified by user and paper
 *
 * @author Yuxuan Liu
 *
 * @date 2019/7/4
 */
@Embeddable
public class PaperAnswerPrimaryKey implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id")
    private Paper paper;

    private int times;

    public PaperAnswerPrimaryKey(User user, Paper paper, int times) {
        this.user = user;
        this.paper = paper;
        this.times = times;
    }

    public PaperAnswerPrimaryKey(User user) {
        this.user = user;
    }

    public PaperAnswerPrimaryKey() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAnswerPrimaryKey that = (PaperAnswerPrimaryKey) o;
        return times == that.times &&
                user.equals(that.user) &&
                paper.equals(that.paper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, paper, times);
    }
}
