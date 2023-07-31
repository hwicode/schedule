package hwicode.schedule.dailyschedule.checklist.exception;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskCheckerNotFoundException extends BusinessException {

    public TaskCheckerNotFoundException() {
        super("과제 체커를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
