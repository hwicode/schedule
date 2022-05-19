package hwicode.schedule.controller.request;

public class FeedbackRequest {

    private Long scheduleId;
    private String feedback;

    public Long getScheduleId() {
        return scheduleId;
    }

    public String getFeedback() {
        return feedback;
    }
}
