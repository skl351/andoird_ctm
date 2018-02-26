package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/1.
 */
public class time_select_item implements Serializable {
    private String start_unix_time;
    private String finish_unix_time;
    private String time;
    private String weekday;
    private String full;

    public String getStart_unix_time() {
        return start_unix_time == null ? "" : start_unix_time;
    }

    public void setStart_unix_time(String start_unix_time) {
        this.start_unix_time = start_unix_time == null ? "" : start_unix_time;
    }

    public String getFinish_unix_time() {
        return finish_unix_time == null ? "" : finish_unix_time;
    }

    public void setFinish_unix_time(String finish_unix_time) {
        this.finish_unix_time = finish_unix_time == null ? "" : finish_unix_time;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    public String getWeekday() {
        return weekday == null ? "" : weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday == null ? "" : weekday;
    }

    public String getFull() {
        return full == null ? "" : full;
    }

    public void setFull(String full) {
        this.full = full == null ? "" : full;
    }

    @Override
    public String toString() {
        return "time_select_item{" +
                "start_unix_time='" + start_unix_time + '\'' +
                ", finish_unix_time='" + finish_unix_time + '\'' +
                ", time='" + time + '\'' +
                ", weekday='" + weekday + '\'' +
                ", full='" + full + '\'' +
                '}';
    }
}
