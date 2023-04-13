package hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class StatusNotFoundException extends BusinessException {

    public StatusNotFoundException() {
        super("알 수 없는 진행 상태입니다.", HttpStatus.BAD_REQUEST);
    }
}
