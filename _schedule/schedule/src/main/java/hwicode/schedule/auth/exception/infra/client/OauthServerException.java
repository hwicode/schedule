package hwicode.schedule.auth.exception.infra.client;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OauthServerException extends BusinessException {

    public OauthServerException(Throwable cause) {
        super("Oauth 서버와의 통신이 실패했습니다.", cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
