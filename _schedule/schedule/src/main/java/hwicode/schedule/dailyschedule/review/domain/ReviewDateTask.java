package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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
        return this.reviewDate.equals(reviewDate);
    }

    void addToReviewDate() {
        reviewDate.addReviewDateTask(this);
    }

    ReviewTask cloneTask(DailyToDoList dailyToDoList) {
        return reviewTask.cloneTask(dailyToDoList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDateTask that = (ReviewDateTask) o;
        return Objects.equals(id, that.id)
                && Objects.equals(reviewTask, that.reviewTask)
                && Objects.equals(reviewDate, that.reviewDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviewTask, reviewDate);
    }
}
