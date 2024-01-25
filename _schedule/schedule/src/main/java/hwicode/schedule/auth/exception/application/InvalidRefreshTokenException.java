package hwicode.schedule.auth.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends BusinessException {

    public InvalidRefreshTokenException() {
        super("Refresh 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
