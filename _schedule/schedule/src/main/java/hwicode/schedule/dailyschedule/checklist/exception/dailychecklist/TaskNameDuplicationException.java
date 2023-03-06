package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

public class TaskNameDuplicationException extends RuntimeException {

    public TaskNameDuplicationException() {
        super("과제의 이름이 중복되었습니다.");
    }
}
