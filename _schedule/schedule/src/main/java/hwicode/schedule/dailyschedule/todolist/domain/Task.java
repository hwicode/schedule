package hwicode.schedule.dailyschedule.todolist.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyToDoList dailyToDoList;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Importance importance;

    @Transient
    private final List<ReviewDateTask> reviewDateTasks = new ArrayList<>();

    public Task(DailyToDoList dailyToDoList, String name) {
        this.dailyToDoList = dailyToDoList;
        this.name = name;
    }

    public void initialize(Priority priority, Importance importance) {
        this.priority = priority;
        this.importance = importance;
    }

    public boolean changePriority(Priority priority) {
        if (this.priority == priority) {
            return false;
        }
        this.priority = priority;
        return true;
    }

    public boolean changeImportance(Importance importance) {
       if (this.importance == importance) {
           return false;
       }
       this.importance = importance;
       return true;
    }

    public List<ReviewDateTask> review(List<ReviewDate> reviewDates) {
        List<ReviewDateTask> uniqueReviewDateTasks = reviewDates.stream()
                .filter(this::isUnique)
                .map(reviewDate -> new ReviewDateTask(this, reviewDate))
                .collect(Collectors.toList());

        reviewDateTasks.addAll(uniqueReviewDateTasks);
        return uniqueReviewDateTasks;
    }

    private boolean isUnique(ReviewDate reviewDate) {
        LocalDate date = reviewDate.getDate();
        boolean isDuplicate = reviewDateTasks.stream()
                .anyMatch(reviewDateTask -> reviewDateTask.isSameDate(date));
        return !isDuplicate;
    }

    public Long getId() {
        return this.id;
    }
}
