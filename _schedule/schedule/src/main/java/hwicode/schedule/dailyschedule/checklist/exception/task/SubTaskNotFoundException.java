package hwicode.schedule.dailyschedule.checklist.exception.task;

public class SubTaskNotFoundException extends RuntimeException {

    public SubTaskNotFoundException() {
        super("서브 과제를 찾을 수 없습니다.");
    }
}
