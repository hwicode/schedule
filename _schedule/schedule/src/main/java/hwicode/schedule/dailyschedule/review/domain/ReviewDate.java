package hwicode.schedule.dailyschedule.review.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @OneToMany(mappedBy = "reviewDate")
    private final Set<ReviewDateTask> reviewDateTasks = new HashSet<>();

    public ReviewDate(LocalDate date) {
        this.date = date;
    }

    void addReviewDateTask(ReviewDateTask reviewDateTask) {
        this.reviewDateTasks.add(reviewDateTask);
    }

//    public List<Task> createTodayReviewTasks(DailyToDoList dailyToDoList) {
//        return reviewDateTasks.stream()
//                .map(reviewDateTask -> reviewDateTask.cloneTask(dailyToDoList))
//                .collect(Collectors.toList());
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDate that = (ReviewDate) o;
        return Objects.equals(id, that.id)
                && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
