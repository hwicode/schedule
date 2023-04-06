package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNotAllDoneException extends ChecklistBusinessException {

    public SubTaskCheckerNotAllDoneException() {
        super("서브 과제가 전부 DONE 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
