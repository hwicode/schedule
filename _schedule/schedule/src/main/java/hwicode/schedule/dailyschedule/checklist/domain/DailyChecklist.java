package hwicode.schedule.dailyschedule.checklist.domain;

import java.util.ArrayList;
import java.util.List;

public class DailyChecklist {

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
        }
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

}
