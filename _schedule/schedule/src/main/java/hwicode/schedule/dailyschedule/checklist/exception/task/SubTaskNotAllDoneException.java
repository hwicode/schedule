package hwicode.schedule.dailyschedule.checklist.exception.task;

public class SubTaskNotAllDoneException extends RuntimeException {

    public SubTaskNotAllDoneException() {
        super("서브 과제가 전부 DONE 상태가 아닙니다.");
    }
}
