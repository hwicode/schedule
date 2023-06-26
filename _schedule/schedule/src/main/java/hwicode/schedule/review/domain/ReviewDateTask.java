package hwicode.schedule.review.domain;

import java.time.LocalDate;

public class ReviewDateTask {

    private ReviewTask reviewTask;
    private ReviewDate reviewDate;

    public ReviewDateTask(ReviewTask reviewTask, ReviewDate reviewDate) {
        this.reviewTask = reviewTask;
        this.reviewDate = reviewDate;
    }

    boolean isSameDate(LocalDate date) {
        return reviewDate.isSame(date);
    }
}
