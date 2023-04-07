package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @Enumerated(value = EnumType.STRING)
    private Importance importance;

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

    String changeTaskName(String name) {
        this.name = name;
        return this.name;
    }

    public Priority changePriority(Priority priority) {
        this.priority = priority;
        return this.priority;
    }

    public Importance changeImportance(Importance importance) {
        this.importance = importance;
        return this.importance;
    }

    public SubTask createSubTask(String subTaskName) {
        validateSubTaskName(subTaskName);

        SubTask subTask = new SubTask(this, subTaskName);
        subTasks.add(subTask);

        return subTask;
    }

    public String changeSubTaskName(String subTaskName, String newSubTaskName) {
        validateSubTaskName(newSubTaskName);
        return findSubTaskBy(subTaskName).changeSubTaskName(newSubTaskName);
    }

    private void validateSubTaskName(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication || name == null) {
            throw new IllegalArgumentException();
        }
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }
}
