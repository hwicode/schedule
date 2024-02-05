package hwicode.schedule.auth.exception.infra.client;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OauthIdTokenException extends BusinessException {

    public OauthIdTokenException() {
        super("Oauth id token을 디코드하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
