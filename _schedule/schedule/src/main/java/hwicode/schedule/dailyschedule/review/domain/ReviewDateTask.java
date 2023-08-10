package hwicode.schedule.dailyschedule.review.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewDateTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewTask reviewTask;

    @JoinColumn(name = "review_date_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewDate reviewDate;

    ReviewDateTask(ReviewTask reviewTask, ReviewDate reviewDate) {
        this.reviewTask = reviewTask;
        this.reviewDate = reviewDate;
    }

    boolean isSameDate(ReviewDate reviewDate) {
        return this.reviewDate.getDate()
                .equals(reviewDate.getDate());
    }

    public ReviewTask getReviewTask() {
        return reviewTask;
    }

}
