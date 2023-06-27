package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "task")
@Entity
public class TaskChecker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyChecklist dailyChecklist;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @ColumnDefault(value = "NORMAL")
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "taskChecker", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTaskChecker> subTaskCheckers = new ArrayList<>();

    public TaskChecker(DailyChecklist dailyChecklist, String name, Difficulty difficulty) {
        this.dailyChecklist = dailyChecklist;
        this.name = name;
        this.taskStatus = TaskStatus.TODO;
        this.difficulty = difficulty;
    }

    public String changeSubTaskCheckerName(String subTaskCheckerName, String newSubTaskCheckerName) {
        validateSubTaskCheckerDuplication(newSubTaskCheckerName);
        return findSubTaskCheckerBy(subTaskCheckerName).changeName(newSubTaskCheckerName);
    }

    SubTaskChecker createSubTaskChecker(String subTaskCheckerName) {
        validateSubTaskCheckerDuplication(subTaskCheckerName);
        SubTaskChecker subTaskChecker = new SubTaskChecker(this, subTaskCheckerName);
        subTaskCheckers.add(subTaskChecker);

        if (this.taskStatus == TaskStatus.DONE) {
            changeToProgress();
        }
        return subTaskChecker;
    }

    private void validateSubTaskCheckerDuplication(String name) {
        boolean duplication = subTaskCheckers.stream()
                .anyMatch(subTaskChecker -> subTaskChecker.isSame(name));

        if (duplication) {
            throw new SubTaskCheckerNameDuplicationException();
        }
    }

    TaskStatus changeSubTaskStatus(String name, SubTaskStatus subTaskStatus) {
        findSubTaskCheckerBy(name).changeStatus(subTaskStatus);
        checkTaskStatusConditions(subTaskStatus);

        return this.taskStatus;
    }

    private void checkTaskStatusConditions(SubTaskStatus subTaskStatus) {
        if (this.taskStatus == TaskStatus.DONE && subTaskStatus != SubTaskStatus.DONE) {
            changeToProgress();
        }

        if (this.taskStatus == TaskStatus.TODO && subTaskStatus != SubTaskStatus.TODO) {
            changeToProgress();
        }
    }

    TaskStatus deleteSubTaskChecker(String name) {
        subTaskCheckers.remove(findSubTaskCheckerBy(name));
        return this.taskStatus;
    }

    void makeDone() {
        subTaskCheckers.forEach(subTaskChecker -> subTaskChecker.changeStatus(SubTaskStatus.DONE));
        changeToDone();
    }

    private SubTaskChecker findSubTaskCheckerBy(String name) {
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
                .filter(subTaskChecker -> subTaskChecker.isSameStatus(subTaskStatus))
                .count();

        return count == subTaskCheckers.size();
    }

    TaskStatus changeToProgress() {
        this.taskStatus = TaskStatus.PROGRESS;
        return this.taskStatus;
    }

    String changeName(String name) {
        this.name = name;
        return this.name;
    }

    Difficulty changeDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this.difficulty;
    }

    TaskStatus getTaskStatus() {
        return this.taskStatus;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
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

