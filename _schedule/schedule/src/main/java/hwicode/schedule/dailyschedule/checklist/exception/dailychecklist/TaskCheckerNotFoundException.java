package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskCheckerNotFoundException extends BusinessException {

    public TaskCheckerNotFoundException() {
        super("과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
