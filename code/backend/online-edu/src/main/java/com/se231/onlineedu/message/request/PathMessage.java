package com.se231.onlineedu.message.request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu
 */
public class PathMessage {
    List<Integer> path = new ArrayList<>();

    public List<Integer> getPath() {
        return path;
    }

    public void setPath(List<Integer> path) {
        this.path = path;
    }
}
