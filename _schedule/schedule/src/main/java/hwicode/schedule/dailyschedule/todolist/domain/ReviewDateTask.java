package hwicode.schedule.dailyschedule.todolist.domain;

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
    private Task task;

    @JoinColumn(name = "review_date_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewDate reviewDate;

    public ReviewDateTask(Task task, ReviewDate reviewDate) {
        this.task = task;
        this.reviewDate = reviewDate;
    }

    boolean isSameDate(ReviewDate reviewDate) {
        return this.reviewDate.equals(reviewDate);
    }

    void addToReviewDate() {
        reviewDate.addReviewDateTask(this);
    }

    Task cloneTask(DailyToDoList dailyToDoList) {
        return task.cloneTask(dailyToDoList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDateTask that = (ReviewDateTask) o;
        return Objects.equals(task, that.task)
                && Objects.equals(reviewDate, that.reviewDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, reviewDate);
    }
}
