package hwicode.schedule.auth.exception.infra.token;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidOauthUserException extends BusinessException {

    public InvalidOauthUserException() {
        super("유저의 id가 없어서 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
