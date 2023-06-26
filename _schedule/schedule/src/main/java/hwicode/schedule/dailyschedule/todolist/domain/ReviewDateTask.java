package hwicode.schedule.dailyschedule.todolist.domain;

import java.time.LocalDate;

public class ReviewDateTask {

    private Task task;
    private ReviewDate reviewDate;

    public ReviewDateTask(Task task, ReviewDate reviewDate) {
        this.task = task;
        this.reviewDate = reviewDate;
    }

    boolean isSameDate(LocalDate date) {
        return reviewDate.isSame(date);
    }
}
