package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by oceanzhang on 16/3/28.
 */
public class TimeSelect implements Serializable{
    private long start_unix_time;

    private long finish_unix_time;

    private String time;

    private int weekday;

    private int full;

    public long getStart_unix_time() {
        return start_unix_time;
    }

    public void setStart_unix_time(long start_unix_time) {
        this.start_unix_time = start_unix_time;
    }

    public long getFinish_unix_time() {
        return finish_unix_time;
    }

    public void setFinish_unix_time(long finish_unix_time) {
        this.finish_unix_time = finish_unix_time;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    @Override
    public String toString() {
        return "TimeSelect{" +
                "start_unix_time=" + start_unix_time +
                ", finish_unix_time=" + finish_unix_time +
                ", time='" + time + '\'' +
                ", weekday=" + weekday +
                ", full=" + full +
                '}';
    }
}
