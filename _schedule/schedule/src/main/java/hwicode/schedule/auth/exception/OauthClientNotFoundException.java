package hwicode.schedule.auth.exception;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OauthClientNotFoundException extends BusinessException {

    public OauthClientNotFoundException() {
        super("Oauth 프로바이더가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
