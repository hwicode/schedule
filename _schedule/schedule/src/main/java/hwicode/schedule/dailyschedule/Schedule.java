package hwicode.schedule.dailyschedule;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void changeTaskDifficulty(String name, Difficulty difficulty) {
        findTaskBy(name).changeDifficulty(difficulty);
    }

    private Task findTaskBy(String name) {
        return tasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void changeToTodo(String name) {
        findTaskBy(name).changeToTodo();
    }

    public void changeToProgress(String name) {
        findTaskBy(name).changeToProgress();
    }

    public void changeToDone(String name) {
        findTaskBy(name).changeToDone();
    }

    public int getTodayDonePercent() {
        return 0;
    }

    public int getTotalDifficultyScore() {
        return tasks.stream()
                .mapToInt(Task::getDifficultyScore)
                .sum();
    }
}
