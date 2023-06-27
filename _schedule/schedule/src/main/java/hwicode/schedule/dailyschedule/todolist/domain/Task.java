package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private Difficulty difficulty;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @Transient
    private final List<ReviewDateTask> reviewDateTasks = new ArrayList<>();

    @Transient
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task(DailyToDoList dailyToDoList, String name) {
        this.dailyToDoList = dailyToDoList;
        this.name = name;
        this.taskStatus = TaskStatus.TODO;
    }

    Task(DailyToDoList dailyToDoList, String name, Priority priority, Importance importance, Difficulty difficulty, List<SubTask> subTasks) {
        this.dailyToDoList = dailyToDoList;
        this.name = name;
        this.priority = priority;
        this.importance = importance;
        this.difficulty = difficulty;
        this.taskStatus = TaskStatus.TODO;
        this.subTasks.addAll(subTasks);
    }

    Task cloneTask(DailyToDoList dailyToDoList) {
        List<SubTask> clonedSubTasks = this.subTasks.stream()
                .map(SubTask::cloneSubTask)
                .collect(Collectors.toList());
        return new Task(dailyToDoList, this.name, this.priority, this.importance, this.difficulty, clonedSubTasks);
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

        uniqueReviewDateTasks.forEach(ReviewDateTask::addToReviewDate);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(name, task.name)
                && priority == task.priority
                && importance == task.importance
                && difficulty == task.difficulty
                && taskStatus == task.taskStatus
                && Objects.equals(subTasks, task.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, priority, importance, difficulty, taskStatus, subTasks);
    }
}
