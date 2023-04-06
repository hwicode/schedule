package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskNotFoundException;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class DailyChecklist {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "dailyChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TaskChecker> taskCheckers = new ArrayList<>();

    public void addTask(TaskChecker taskChecker) {
        validateTaskDuplication(taskChecker.getName());
        taskCheckers.add(taskChecker);
        taskChecker.savedInChecklist(this);
    }

    private void validateTaskDuplication(String name) {
        boolean duplication = taskCheckers.stream()
                .anyMatch(task -> task.isSame(name));

        if (duplication) {
            throw new TaskNameDuplicationException();
        }
    }

    public Difficulty changeTaskDifficulty(String name, Difficulty difficulty) {
        return findTaskBy(name).changeDifficulty(difficulty);
    }

    public void deleteTask(String name) {
        taskCheckers.remove(findTaskBy(name));
    }

    public TaskStatus changeTaskStatus(String name, TaskStatus taskStatus) {
        switch (taskStatus) {
            case TODO:
                return findTaskBy(name).changeToTodo();
            case PROGRESS:
                return findTaskBy(name).changeToProgress();
            case DONE:
                return findTaskBy(name).changeToDone();
            default:
                throw new StatusNotFoundException();
        }
    }

    public void makeTaskDone(String taskName) {
        findTaskBy(taskName).makeDone();
    }

    private TaskChecker findTaskBy(String name) {
        return taskCheckers.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(TaskNotFoundException::new);
    }

    public void addSubTask(String taskName, SubTaskChecker subTaskChecker) {
        findTaskBy(taskName).addSubTask(subTaskChecker);
    }

    public TaskStatus changeSubTaskStatus(String taskName, String subTaskName, SubTaskStatus subTaskStatus) {
        return findTaskBy(taskName).changeSubTaskStatus(subTaskName, subTaskStatus);
    }

    public void deleteSubTask(String taskName, String subTaskName) {
        findTaskBy(taskName).deleteSubTask(subTaskName);
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
