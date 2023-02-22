package hwicode.schedule.dailyschedule;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        validateTaskDuplication(task.getName());
        tasks.add(task);
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

    private Task findTaskBy(String name) {
        return tasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void changeTaskStatusToTodo(String name) {
        findTaskBy(name).changeToTodo();
    }

    public void changeTaskStatusToProgress(String name) {
        findTaskBy(name).changeToProgress();
    }

    public void changeTaskStatusToDone(String name) {
        findTaskBy(name).changeToDone();
    }

    public int getTodayDonePercent() {
        return (int) (getDoneTaskScore() / getTotalDifficultyScore() * 100);
    }

    private double getDoneTaskScore() {
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

}
