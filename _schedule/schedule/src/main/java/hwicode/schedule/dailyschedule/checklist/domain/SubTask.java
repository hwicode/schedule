package hwicode.schedule.dailyschedule.checklist.domain;

public class SubTask {

    private Status status;
    private final String name;

    public SubTask(String name) {
        this.status = Status.TODO;
        this.name = name;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    void changeStatus(Status status) {
        this.status = status;
    }

    boolean isSameStatus(Status status) {
        return this.status == status;
    }

    String getName() {
        return this.name;
    }
}
