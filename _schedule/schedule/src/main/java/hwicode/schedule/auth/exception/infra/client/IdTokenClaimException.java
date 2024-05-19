package hwicode.schedule.auth.exception.infra.client;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class IdTokenClaimException extends BusinessException {

    public IdTokenClaimException() {
        super("id token의 클레임을 가져오는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
