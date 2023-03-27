package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotFoundException;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_checklist_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyChecklist dailyChecklist;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task(String name) {
        this.name = name;
        this.difficulty = Difficulty.NORMAL;
        this.status = Status.TODO;
    }

    public Task(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
        this.status = Status.TODO;
    }

    Status addSubTask(SubTask subTask) {
        validateSubTaskDuplication(subTask.getName());
        subTasks.add(subTask);
        subTask.savedInTask(this);

        if (this.status.isDone()) {
            changeToProgress();
        }
        return this.status;
    }

    private void validateSubTaskDuplication(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new SubTaskNameDuplicationException();
        }
    }

    Status changeSubTaskStatus(String name, Status subTaskStatus) {
        findSubTaskBy(name).changeStatus(subTaskStatus);
        checkTaskStatusConditions(subTaskStatus);

        return this.status;
    }

    private void checkTaskStatusConditions(Status subTaskStatus) {
        if (this.status.isDone() && !subTaskStatus.isDone()) {
            changeToProgress();
        }

        else if (this.status.isTodo() && !subTaskStatus.isTodo()) {
            changeToProgress();
        }
    }

    Status deleteSubTask(String name) {
        subTasks.remove(findSubTaskBy(name));
        return this.status;
    }

    void makeDone() {
        subTasks.forEach(subTask -> subTask.changeStatus(Status.DONE));
        changeToDone();
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(SubTaskNotFoundException::new);
    }

    Status changeToDone() {
        boolean isAllDone = isAllSameStatus(Status.DONE);
        if (!isAllDone) {
            throw new SubTaskNotAllDoneException();
        }

        this.status = Status.DONE;
        return this.status;
    }

    Status changeToTodo() {
        boolean isAllTodo = isAllSameStatus(Status.TODO);
        if (!isAllTodo) {
            throw new SubTaskNotAllTodoException();
        }

        this.status = Status.TODO;
        return this.status;
    }

    private boolean isAllSameStatus(Status status) {
        int count = (int) subTasks.stream()
                .filter(subTask -> subTask.isSameStatus(status))
                .count();

        return count == subTasks.size();
    }

    Status changeToProgress() {
        this.status = Status.PROGRESS;
        return this.status;
    }

    void savedInChecklist(DailyChecklist dailyChecklist) {
        this.dailyChecklist = dailyChecklist;
    }

    Difficulty changeDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this.difficulty;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    public boolean isDone() {
        return this.status.isDone();
    }

    public int getDifficultyScore() {
        return difficulty.getValue();
    }
}

