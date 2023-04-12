package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "daily_to_do_list")
@Entity
public class DailyChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "dailyChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TaskChecker> taskCheckers = new ArrayList<>();

    public void addTaskChecker(TaskChecker taskChecker) {
        validateTaskCheckerDuplication(taskChecker.getName());
        taskCheckers.add(taskChecker);
        taskChecker.savedInChecklist(this);
    }

    private void validateTaskCheckerDuplication(String name) {
        boolean duplication = taskCheckers.stream()
                .anyMatch(taskChecker -> taskChecker.isSame(name));

        if (duplication) {
            throw new TaskCheckerNameDuplicationException();
        }
    }

    public Difficulty changeDifficulty(String name, Difficulty difficulty) {
        return findTaskCheckerBy(name).changeDifficulty(difficulty);
    }

    public void deleteTaskChecker(String name) {
        taskCheckers.remove(findTaskCheckerBy(name));
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

    public void addSubTaskChecker(String taskCheckerName, SubTaskChecker subTaskChecker) {
        findTaskCheckerBy(taskCheckerName).addSubTaskChecker(subTaskChecker);
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
        return taskCheckers.stream()
                .mapToInt(TaskChecker::getDifficultyScore)
                .sum();
    }

    public Long getId() {
        return id;
    }
}
