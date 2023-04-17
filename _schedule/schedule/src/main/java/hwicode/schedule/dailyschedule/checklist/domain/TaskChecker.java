package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.dailyschedule_domain.Difficulty;
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

    @JoinColumn(name = "daily_to_do_list_id")
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

    public TaskChecker(String name, Difficulty difficulty) {
        this.name = name;
        this.taskStatus = TaskStatus.TODO;
        this.difficulty = difficulty;
    }

    public TaskChecker(String name, TaskStatus taskStatus, Difficulty difficulty) {
        this.name = name;
        this.taskStatus = taskStatus;
        this.difficulty = difficulty;
    }

    public String changeSubTaskCheckerName(String subTaskCheckerName, String newSubTaskCheckerName) {
        validateSubTaskCheckerDuplication(newSubTaskCheckerName);
        return findSubTaskCheckerBy(subTaskCheckerName).changeName(newSubTaskCheckerName);
    }

    TaskStatus addSubTaskChecker(SubTaskChecker subTaskChecker) {
        validateSubTaskCheckerDuplication(subTaskChecker.getName());
        subTaskCheckers.add(subTaskChecker);
        subTaskChecker.savedInTaskChecker(this);

        if (this.taskStatus == TaskStatus.DONE) {
            changeToProgress();
        }
        return this.taskStatus;
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
        else if (this.taskStatus == TaskStatus.TODO && subTaskStatus != SubTaskStatus.TODO) {
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

    void savedInChecklist(DailyChecklist dailyChecklist) {
        this.dailyChecklist = dailyChecklist;
    }

    String changeName(String name) {
        this.name = name;
        return this.name;
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

