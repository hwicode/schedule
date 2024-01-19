package hwicode.schedule.auth.exception.infra;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OauthIdTokenException extends BusinessException {

    public OauthIdTokenException() {
        super("Oauth id_token을 객체로 변환하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
