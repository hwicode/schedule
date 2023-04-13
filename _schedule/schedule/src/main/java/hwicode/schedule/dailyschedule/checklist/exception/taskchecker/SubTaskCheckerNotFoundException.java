package hwicode.schedule.dailyschedule.checklist.exception.taskchecker;

import hwicode.schedule.common.exception_handler.BusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNotFoundException extends BusinessException {

    public SubTaskCheckerNotFoundException() {
        super("서브 과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
