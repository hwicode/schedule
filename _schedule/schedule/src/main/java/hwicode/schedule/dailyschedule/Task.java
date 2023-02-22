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

    //todo: subtask name 중복 validation 추가해야 함
    public Status addSubTask(SubTask subTask) {
        subTasks.add(subTask);

        if (this.status.isDone()) {
            changeToProgress();
        }
        return this.status;
    }

    public Status changeSubTaskStatus(String name, Status subTaskStatus) {
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

    public Status deleteSubTask(String name) {
        subTasks.remove(findSubTaskBy(name));
        return this.status;
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Status changeToDone() {
        boolean isAllDone = isAllSameStatus(Status.DONE);
        if (!isAllDone) {
            throw new IllegalStateException();
        }

        this.status = Status.DONE;
        return this.status;
    }

    public Status changeToTodo() {
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

    public Status changeToProgress() {
        this.status = Status.PROGRESS;
        return this.status;
    }

    public void changeDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isSame(String name) {
        return this.name.equals(name);
    }

    public boolean isSameStatus(Status status) {
        return this.status == status;
    }

    public int getDifficultyScore() {
        return difficulty.getValue();
    }
}
