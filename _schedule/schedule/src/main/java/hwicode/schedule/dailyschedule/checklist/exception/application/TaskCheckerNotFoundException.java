package hwicode.schedule.dailyschedule.checklist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskCheckerNotFoundException extends BusinessException {

    public TaskCheckerNotFoundException() {
        super("과제 체커가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
