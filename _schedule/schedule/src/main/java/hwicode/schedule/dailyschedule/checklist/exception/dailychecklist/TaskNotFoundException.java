package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        super("과제를 찾을 수 없습니다.");
    }
}
