package hwicode.schedule.dailyschedule.checklist.exception.task;

public class SubTaskNameDuplicationException extends RuntimeException {

    public SubTaskNameDuplicationException() {
        super("서브 과제의 이름이 중복되었습니다.");
    }
}
