package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNameDuplicationException extends ChecklistBusinessException {

    public SubTaskCheckerNameDuplicationException() {
        super("서브 과제의 이름이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}