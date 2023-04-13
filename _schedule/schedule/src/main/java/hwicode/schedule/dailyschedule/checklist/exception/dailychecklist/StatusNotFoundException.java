package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.common.exception_handler.BusinessException;
import org.springframework.http.HttpStatus;

public class StatusNotFoundException extends BusinessException {

    public StatusNotFoundException() {
        super("알 수 없는 진행 상태입니다.", HttpStatus.BAD_REQUEST);
    }
}
