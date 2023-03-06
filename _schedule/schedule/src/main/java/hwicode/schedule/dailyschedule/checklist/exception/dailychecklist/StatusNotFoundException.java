package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

public class StatusNotFoundException extends RuntimeException {

    public StatusNotFoundException() {
        super("알 수 없는 진행 상태입니다.");
    }
}
