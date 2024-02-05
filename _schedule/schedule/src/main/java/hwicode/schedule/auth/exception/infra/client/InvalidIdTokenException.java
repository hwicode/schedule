package hwicode.schedule.auth.exception.infra.client;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidIdTokenException extends BusinessException {

    public InvalidIdTokenException() {
        super("id token으로 null은 올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
