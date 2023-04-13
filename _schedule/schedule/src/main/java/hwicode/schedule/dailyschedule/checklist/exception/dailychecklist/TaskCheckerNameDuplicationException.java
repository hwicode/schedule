package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskCheckerNameDuplicationException extends BusinessException {

    public TaskCheckerNameDuplicationException() {
        super("과제의 이름이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
