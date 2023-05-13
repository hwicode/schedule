package hwicode.schedule.dailyschedule.todolist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotValidExternalRequestException extends BusinessException {

    private final BusinessException externalException;

    public NotValidExternalRequestException(BusinessException externalException) {
        super("잘못된 요청 값이 있습니다.", HttpStatus.BAD_REQUEST);
        this.externalException = externalException;
    }

    public BusinessException getExternalException() {
        return externalException;
    }
}
