package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotFoundException;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "task")
@Entity
public class TaskChecker {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_checklist_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyChecklist dailyChecklist;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "taskChecker", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTaskChecker> subTaskCheckers = new ArrayList<>();

    public TaskChecker(String name) {
        this.name = name;
        this.taskStatus = TaskStatus.TODO;
        this.difficulty = Difficulty.NORMAL;
    }

    public TaskChecker(String name, TaskStatus taskStatus, Difficulty difficulty) {
        this.name = name;
        this.taskStatus = taskStatus;
        this.difficulty = difficulty;
    }

    TaskStatus addSubTask(SubTaskChecker subTaskChecker) {
        validateSubTaskDuplication(subTaskChecker.getName());
        subTaskCheckers.add(subTaskChecker);
        subTaskChecker.savedInTask(this);

        if (this.taskStatus == TaskStatus.DONE) {
            changeToProgress();
        }
        return this.taskStatus;
    }

    private void validateSubTaskDuplication(String name) {
        boolean duplication = subTaskCheckers.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new SubTaskCheckerNameDuplicationException();
        }
    }

    TaskStatus changeSubTaskStatus(String name, SubTaskStatus subTaskStatus) {
        findSubTaskBy(name).changeStatus(subTaskStatus);
        checkTaskStatusConditions(subTaskStatus);

        return this.taskStatus;
    }

    private void checkTaskStatusConditions(SubTaskStatus subTaskStatus) {
        if (this.taskStatus == TaskStatus.DONE && subTaskStatus != SubTaskStatus.DONE) {
            changeToProgress();
        }

        else if (this.taskStatus == TaskStatus.TODO && subTaskStatus != SubTaskStatus.TODO) {
            changeToProgress();
        }
    }

    TaskStatus deleteSubTask(String name) {
        subTaskCheckers.remove(findSubTaskBy(name));
        return this.taskStatus;
    }

    void makeDone() {
        subTaskCheckers.forEach(subTask -> subTask.changeStatus(SubTaskStatus.DONE));
        changeToDone();
    }

    private SubTaskChecker findSubTaskBy(String name) {
        return subTaskCheckers.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(SubTaskCheckerNotFoundException::new);
    }

    TaskStatus changeToDone() {
        boolean isAllDone = isAllSameStatus(SubTaskStatus.DONE);
        if (!isAllDone) {
            throw new SubTaskCheckerNotAllDoneException();
        }

        this.taskStatus = TaskStatus.DONE;
        return this.taskStatus;
    }

    TaskStatus changeToTodo() {
        boolean isAllTodo = isAllSameStatus(SubTaskStatus.TODO);
        if (!isAllTodo) {
            throw new SubTaskCheckerNotAllTodoException();
        }

        this.taskStatus = TaskStatus.TODO;
        return this.taskStatus;
    }

    private boolean isAllSameStatus(SubTaskStatus subTaskStatus) {
        int count = (int) subTaskCheckers.stream()
                .filter(subTask -> subTask.isSameStatus(subTaskStatus))
                .count();

        return count == subTaskCheckers.size();
    }

    TaskStatus changeToProgress() {
        this.taskStatus = TaskStatus.PROGRESS;
        return this.taskStatus;
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
        return this.taskStatus == TaskStatus.DONE;
    }

    public int getDifficultyScore() {
        return difficulty.getValue();
    }
}

