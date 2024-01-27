package hwicode.schedule.auth.exception.infra.token;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super("토큰이 유효하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
