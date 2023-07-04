package hwicode.schedule.dailyschedule.review.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate date;

    ReviewDateTask(ReviewTask reviewTask, ReviewDate reviewDate) {
        this.reviewTask = reviewTask;
        this.reviewDate = reviewDate;
        this.date = reviewDate.getDate();
    }

    boolean isSameDate(ReviewDate reviewDate) {
        return this.date.equals(reviewDate.getDate());
    }

}
