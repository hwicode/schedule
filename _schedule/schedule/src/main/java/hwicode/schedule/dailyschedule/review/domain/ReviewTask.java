package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class ReviewTask {

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

    @ColumnDefault(value = "NORMAL")
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @OneToMany(mappedBy = "reviewTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewDateTask> reviewDateTasks = new ArrayList<>();

    @OneToMany(mappedBy = "reviewTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewSubTask> reviewSubTasks = new ArrayList<>();

    ReviewTask(DailyToDoList dailyToDoList, String name, Priority priority, Importance importance, Difficulty difficulty, List<ReviewSubTask> reviewSubTasks) {
        this.dailyToDoList = dailyToDoList;
        this.name = name;
        this.priority = priority;
        this.importance = importance;
        this.difficulty = difficulty;
        this.taskStatus = TaskStatus.TODO;
        this.reviewSubTasks.addAll(reviewSubTasks);
    }

    // 테스트 코드에서만 사용되는 생성자!
    public ReviewTask(String name, TaskStatus taskStatus, List<ReviewSubTask> reviewSubTasks) {
        this.name = name;
        this.taskStatus = taskStatus;
        this.reviewSubTasks.addAll(reviewSubTasks);
    }

    ReviewTask cloneTask(DailyToDoList dailyToDoList) {
        List<ReviewSubTask> clonedReviewSubTasks = this.reviewSubTasks.stream()
                .map(ReviewSubTask::cloneSubTask)
                .collect(Collectors.toList());
        return new ReviewTask(dailyToDoList, this.name, this.priority, this.importance, this.difficulty, clonedReviewSubTasks);
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
        boolean isDuplicate = reviewDateTasks.stream()
                .anyMatch(reviewDateTask -> reviewDateTask.isSameDate(reviewDate));
        return !isDuplicate;
    }

    public Long getId() {
        return this.id;
    }

}
