package hwicode.schedule.dailyschedule;

public class SubTask {

    private Status status;
    private String name;

    public SubTask(String name) {
        this.status = Status.TODO;
        this.name = name;
    }

    boolean isDone() {
        return status == Status.DONE;
    }

    boolean isTodo() {
        return status == Status.TODO;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    void changeStatus(Status status) {
        this.status = status;
    }
}
