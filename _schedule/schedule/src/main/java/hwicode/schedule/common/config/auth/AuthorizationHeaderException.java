package hwicode.schedule.common.config.auth;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AuthorizationHeaderException extends BusinessException {

    public AuthorizationHeaderException() {
        super("HTTP Header에 Authorization이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
