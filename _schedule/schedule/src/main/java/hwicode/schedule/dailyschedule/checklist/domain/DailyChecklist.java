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
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        validateTaskDuplication(task.getName());
        tasks.add(task);
        task.savedInChecklist(this);
    }

    private void validateTaskDuplication(String name) {
        boolean duplication = tasks.stream()
                .anyMatch(task -> task.isSame(name));

        if (duplication) {
            throw new TaskNameDuplicationException();
        }
    }

    public Difficulty changeTaskDifficulty(String name, Difficulty difficulty) {
        return findTaskBy(name).changeDifficulty(difficulty);
    }

    public void deleteTask(String name) {
        tasks.remove(findTaskBy(name));
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

    private Task findTaskBy(String name) {
        return tasks.stream()
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
        return tasks.stream()
                .filter(Task::isDone)
                .mapToInt(Task::getDifficultyScore)
                .sum();
    }

    public int getTotalDifficultyScore() {
        return tasks.stream()
                .mapToInt(Task::getDifficultyScore)
                .sum();
    }

    public Long getId() {
        return id;
    }
}
