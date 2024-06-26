package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = {"id", "name", "priority", "importance", "difficulty", "reviewSubTasks"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class ReviewTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewList reviewList;

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

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "reviewTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewDateTask> reviewDateTasks = new ArrayList<>();

    @OneToMany(mappedBy = "reviewTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewSubTask> reviewSubTasks = new ArrayList<>();

    public ReviewTask(ReviewList reviewList, String name, Priority priority, Importance importance, Difficulty difficulty, Long userId) {
        this.reviewList = reviewList;
        this.name = name;
        this.priority = priority;
        this.importance = importance;
        this.difficulty = difficulty;
        this.taskStatus = TaskStatus.TODO;
        this.userId = userId;
    }

    public ReviewTask cloneTask(ReviewList reviewList) {
        ReviewTask clonedTask = new ReviewTask(reviewList, this.name, this.priority, this.importance, this.difficulty, this.userId);
        List<ReviewSubTask> clonedReviewSubTasks = this.reviewSubTasks.stream()
                .map(reviewSubTask -> reviewSubTask.cloneSubTask(clonedTask))
                .collect(Collectors.toList());
        clonedTask.addAllToReviewSubTasks(clonedReviewSubTasks);
        return clonedTask;
    }

    void addAllToReviewSubTasks(List<ReviewSubTask> clonedReviewSubTasks) {
        this.reviewSubTasks.addAll(clonedReviewSubTasks);
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

    public Long getUserId() {
        return userId;
    }
}
