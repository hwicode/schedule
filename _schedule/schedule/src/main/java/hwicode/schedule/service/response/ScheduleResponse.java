package hwicode.schedule.service.response;

import hwicode.schedule.domain.Task;

import java.util.List;

public class ScheduleResponse {

    private int dayPoint;
    private int dayPercentage;
    private String dayFeedback;
    private String scheduleDate;
    private List<Task> tasks;

    public ScheduleResponse(int dayPoint, int dayPercentage, String dayFeedback, String scheduleDate, List<Task> tasks) {
        this.dayPoint = dayPoint;
        this.dayPercentage = dayPercentage;
        this.dayFeedback = dayFeedback;
        this.scheduleDate = scheduleDate;
        this.tasks = tasks;
    }

    public int getDayPoint() {
        return dayPoint;
    }

    public int getDayPercentage() {
        return dayPercentage;
    }

    public String getDayFeedback() {
        return dayFeedback;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
