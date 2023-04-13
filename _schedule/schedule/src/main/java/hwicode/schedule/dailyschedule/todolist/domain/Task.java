package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import hwicode.schedule.dailyschedule.todolist.exception.domain.task.SubTaskNameDuplicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_to_do_list_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyToDoList dailyToDoList;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "NORMAL")
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Importance importance;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTask> subTasks = new ArrayList<>();

    Task(DailyToDoList dailyToDoList, TaskCreateDto taskCreateDto) {
        this.dailyToDoList = dailyToDoList;
        this.name = taskCreateDto.getTaskName();
        this.difficulty = taskCreateDto.getDifficulty();
        this.priority = taskCreateDto.getPriority();
        this.importance = taskCreateDto.getImportance();
        this.taskStatus = TaskStatus.TODO;
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

    public SubTask createSubTask(String subTaskName) {
        validateSubTaskName(subTaskName);

        SubTask subTask = new SubTask(this, subTaskName);
        subTasks.add(subTask);

        return subTask;
    }

    private void validateSubTaskName(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new SubTaskNameDuplicationException();
        }
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return this.id;
    }
}
