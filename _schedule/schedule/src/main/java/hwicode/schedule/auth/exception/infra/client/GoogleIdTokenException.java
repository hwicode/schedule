package hwicode.schedule.auth.exception.infra.client;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class GoogleIdTokenException extends BusinessException {

    public GoogleIdTokenException() {
        super("구글 id token의 클레임을 객체로 변환하는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
