package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "daily_schedule")
@Entity
public class DailyChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault(value = "0")
    @Column(nullable = false)
    private int totalDifficultyScore;

    @ColumnDefault(value = "0")
    @Column(nullable = false)
    private int todayDonePercent;

    @OneToMany(mappedBy = "dailyChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TaskChecker> taskCheckers = new ArrayList<>();

    public DailyChecklist() {
        this.totalDifficultyScore = 0;
        this.todayDonePercent = 0;
    }

    public String changeTaskCheckerName(String taskCheckerName, String newTaskCheckerName) {
        validateTaskCheckerDuplication(newTaskCheckerName);
        return findTaskCheckerBy(taskCheckerName).changeName(newTaskCheckerName);
    }

    public TaskChecker createTaskChecker(String taskName, Difficulty difficulty) {
        validateTaskCheckerDuplication(taskName);
        TaskChecker taskChecker = new TaskChecker(this, taskName, difficulty);
        taskCheckers.add(taskChecker);
        totalDifficultyScore += difficulty.getValue();
        calculateTodayDonePercent();
        return taskChecker;
    }

    private void validateTaskCheckerDuplication(String name) {
        boolean duplication = taskCheckers.stream()
                .anyMatch(taskChecker -> taskChecker.isSame(name));

        if (duplication) {
            throw new TaskCheckerNameDuplicationException();
        }
    }

    public Difficulty changeDifficulty(String name, Difficulty difficulty) {
        TaskChecker taskChecker = findTaskCheckerBy(name);
        totalDifficultyScore -= taskChecker.getDifficultyScore();
        totalDifficultyScore += difficulty.getValue();
        calculateTodayDonePercent();
        return taskChecker.changeDifficulty(difficulty);
    }

    public void deleteTaskChecker(String name) {
        TaskChecker taskChecker = findTaskCheckerBy(name);
        totalDifficultyScore -= taskChecker.getDifficultyScore();
        taskCheckers.remove(taskChecker);
        calculateTodayDonePercent();
    }

    public TaskStatus changeTaskStatus(String name, TaskStatus newStatus) {
        TaskChecker taskChecker = findTaskCheckerBy(name);
        TaskStatus updatedStatus = updateStatus(taskChecker, newStatus);

        calculateTodayDonePercent();
        return updatedStatus;
    }

    private TaskStatus updateStatus(TaskChecker taskChecker, TaskStatus newStatus) {
        switch (newStatus) {
            case TODO:
                return taskChecker.changeToTodo();
            case PROGRESS:
                return taskChecker.changeToProgress();
            case DONE:
                return taskChecker.changeToDone();
            default:
                throw new StatusNotFoundException();
        }
    }

    public void makeTaskCheckerToDone(String name) {
        findTaskCheckerBy(name).makeDone();
        calculateTodayDonePercent();
    }

    private TaskChecker findTaskCheckerBy(String name) {
        return taskCheckers.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(TaskCheckerNotFoundException::new);
    }

    public SubTaskChecker createSubTaskChecker(String taskCheckerName, String subTaskCheckerName) {
        SubTaskChecker subTaskChecker = findTaskCheckerBy(taskCheckerName).createSubTaskChecker(subTaskCheckerName);
        calculateTodayDonePercent();
        return subTaskChecker;
    }

    public TaskStatus changeSubTaskStatus(String taskCheckerName, String subTaskCheckerName, SubTaskStatus subTaskStatus) {
        TaskStatus taskStatus = findTaskCheckerBy(taskCheckerName).changeSubTaskStatus(subTaskCheckerName, subTaskStatus);
        calculateTodayDonePercent();
        return taskStatus;
    }

    public void deleteSubTaskChecker(String taskCheckerName, String subTaskCheckerName) {
        findTaskCheckerBy(taskCheckerName).deleteSubTaskChecker(subTaskCheckerName);
        calculateTodayDonePercent();
    }

    private void calculateTodayDonePercent() {
        this.todayDonePercent = (int) (getDoneTasksScore() / getTotalDifficultyScore() * 100);
    }

    private double getDoneTasksScore() {
        return taskCheckers.stream()
                .filter(TaskChecker::isDone)
                .mapToInt(TaskChecker::getDifficultyScore)
                .sum();
    }

    public int getTotalDifficultyScore() {
        return this.totalDifficultyScore;
    }

    public int getTodayDonePercent() {
        return this.todayDonePercent;
    }

    public Long getId() {
        return id;
    }
}
