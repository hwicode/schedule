package hwicode.schedule.dailyschedule;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private Status status;
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task() {
        this.status = Status.TODO;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);

        if (this.status == Status.DONE) {
            this.status = Status.PROGRESS;
        }
    }

    public void changeSubTaskStatus(String name, Status subTaskStatus) {
        findSubTaskBy(name).changeStatus(subTaskStatus);
        checkTaskStatusConditions(subTaskStatus);
    }

    private void checkTaskStatusConditions(Status subTaskStatus) {
        if (this.status == Status.DONE) {
            this.status = Status.PROGRESS;
        }

        else if (this.status == Status.TODO && subTaskStatus != Status.TODO) {
            this.status = Status.PROGRESS;
        }
    }

    public void deleteSubTask(String name) {
        subTasks.remove(findSubTaskBy(name));
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void changeToDone() {
        if (!isAllDone()) {
            throw new IllegalStateException();
        }
        this.status = Status.DONE;
    }

    private boolean isAllDone() {
        int count = (int) subTasks.stream()
                .filter(SubTask::isDone)
                .count();

        return count == subTasks.size();
    }

    public void changeToTodo() {
        if (!isAllTodo()) {
            throw new IllegalStateException();
        }
        this.status = Status.TODO;
    }

    private boolean isAllTodo() {
        int count = (int) subTasks.stream()
                .filter(SubTask::isTodo)
                .count();

        return count == subTasks.size();
    }

    public void changeToProgress() {
        this.status = Status.PROGRESS;
    }

    public Status getStatus() {
        return this.status;
    }
}
