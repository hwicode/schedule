package hwicode.schedule.dailyschedule.checklist.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
            throw new IllegalStateException();
        }
    }

    public void changeTaskDifficulty(String name, Difficulty difficulty) {
        findTaskBy(name).changeDifficulty(difficulty);
    }

    public void deleteTask(String name) {
        tasks.remove(findTaskBy(name));
    }

    public void changeTaskStatus(String name, Status status) {
        switch (status) {
            case TODO:
                findTaskBy(name).changeToTodo();
                break;
            case PROGRESS:
                findTaskBy(name).changeToProgress();
                break;
            case DONE:
                findTaskBy(name).changeToDone();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Task findTaskBy(String name) {
        return tasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void addSubTask(String taskName, SubTask subTask) {
        findTaskBy(taskName).addSubTask(subTask);
    }

    public Status changeSubTaskStatus(String taskName, String subTaskName, Status status) {
        return findTaskBy(taskName).changeSubTaskStatus(subTaskName, status);
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
