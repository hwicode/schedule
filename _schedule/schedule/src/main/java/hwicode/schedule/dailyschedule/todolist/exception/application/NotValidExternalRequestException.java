package hwicode.schedule.dailyschedule.todolist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class NotValidExternalRequestException extends BusinessException {

    private final List<String> externalErrorMessages;

    public NotValidExternalRequestException(List<String> externalErrorMessages) {
        super("외부에 요청할 때, 잘못된 값이 있습니다.", HttpStatus.BAD_REQUEST);
        this.externalErrorMessages = externalErrorMessages;
    }

    public List<String> getExternalErrorMessages() {
        return externalErrorMessages;
    }
}
