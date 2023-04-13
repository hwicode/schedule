package hwicode.schedule.dailyschedule.todolist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotValidExternalRequestException extends BusinessException {

    public NotValidExternalRequestException() {
        super("잘못된 요청 값이 있습니다.", HttpStatus.BAD_REQUEST);
    }
}
