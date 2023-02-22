package hwicode.schedule.dailyschedule;

public enum Status {
    TODO, PROGRESS, DONE;

    public boolean isDone() {
        return this == Status.DONE;
    }

    public boolean isTodo() {
        return this == Status.TODO;
    }
}
