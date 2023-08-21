package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "daily_schedule")
@Entity
public class DailyChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int totalDifficultyScore;

    @OneToMany(mappedBy = "dailyChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TaskChecker> taskCheckers = new ArrayList<>();

    public DailyChecklist() {
        this.totalDifficultyScore = 0;
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
        return taskChecker.changeDifficulty(difficulty);
    }

    public void deleteTaskChecker(String name) {
        TaskChecker taskChecker = findTaskCheckerBy(name);
        totalDifficultyScore -= taskChecker.getDifficultyScore();
        taskCheckers.remove(taskChecker);
    }

    public TaskStatus changeTaskStatus(String name, TaskStatus taskStatus) {
        switch (taskStatus) {
            case TODO:
                return findTaskCheckerBy(name).changeToTodo();
            case PROGRESS:
                return findTaskCheckerBy(name).changeToProgress();
            case DONE:
                return findTaskCheckerBy(name).changeToDone();
            default:
                throw new StatusNotFoundException();
        }
    }

    public void makeTaskCheckerToDone(String name) {
        findTaskCheckerBy(name).makeDone();
    }

    private TaskChecker findTaskCheckerBy(String name) {
        return taskCheckers.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(TaskCheckerNotFoundException::new);
    }

    public SubTaskChecker createSubTaskChecker(String taskCheckerName, String subTaskCheckerName) {
        return findTaskCheckerBy(taskCheckerName).createSubTaskChecker(subTaskCheckerName);
    }

    public TaskStatus changeSubTaskStatus(String taskCheckerName, String subTaskCheckerName, SubTaskStatus subTaskStatus) {
        return findTaskCheckerBy(taskCheckerName).changeSubTaskStatus(subTaskCheckerName, subTaskStatus);
    }

    public void deleteSubTaskChecker(String taskCheckerName, String subTaskCheckerName) {
        findTaskCheckerBy(taskCheckerName).deleteSubTaskChecker(subTaskCheckerName);
    }

    public int getTodayDonePercent() {
        return (int) (getDoneTasksScore() / getTotalDifficultyScore() * 100);
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

    public Long getId() {
        return id;
    }
}
