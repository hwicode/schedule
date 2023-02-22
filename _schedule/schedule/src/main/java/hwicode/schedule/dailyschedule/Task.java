package hwicode.schedule.dailyschedule;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private String name;
    private Status status;
    private Difficulty difficulty;
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task(String name) {
        this.name = name;
        this.difficulty = Difficulty.NORMAL;
        this.status = Status.TODO;
    }

    public Task(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
        this.status = Status.TODO;
    }

    Status addSubTask(SubTask subTask) {
        validateSubTaskDuplication(subTask.getName());
        subTasks.add(subTask);

        if (this.status.isDone()) {
            changeToProgress();
        }
        return this.status;
    }

    private void validateSubTaskDuplication(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new IllegalStateException();
        }
    }

    Status changeSubTaskStatus(String name, Status subTaskStatus) {
        findSubTaskBy(name).changeStatus(subTaskStatus);
        checkTaskStatusConditions(subTaskStatus);

        return this.status;
    }

    private void checkTaskStatusConditions(Status subTaskStatus) {
        if (this.status.isDone() && !subTaskStatus.isDone()) {
            changeToProgress();
        }

        else if (this.status.isTodo() && !subTaskStatus.isTodo()) {
            changeToProgress();
        }
    }

    Status deleteSubTask(String name) {
        subTasks.remove(findSubTaskBy(name));
        return this.status;
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    Status changeToDone() {
        boolean isAllDone = isAllSameStatus(Status.DONE);
        if (!isAllDone) {
            throw new IllegalStateException();
        }

        this.status = Status.DONE;
        return this.status;
    }

    Status changeToTodo() {
        boolean isAllTodo = isAllSameStatus(Status.TODO);
        if (!isAllTodo) {
            throw new IllegalStateException();
        }

        this.status = Status.TODO;
        return this.status;
    }

    private boolean isAllSameStatus(Status status) {
        int count = (int) subTasks.stream()
                .filter(subTask -> subTask.isSameStatus(status))
                .count();

        return count == subTasks.size();
    }

    Status changeToProgress() {
        this.status = Status.PROGRESS;
        return this.status;
    }

    void changeDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    boolean isSameStatus(Status status) {
        return this.status == status;
    }

    int getDifficultyScore() {
        return difficulty.getValue();
    }

    String getName() {
        return this.name;
    }
}
