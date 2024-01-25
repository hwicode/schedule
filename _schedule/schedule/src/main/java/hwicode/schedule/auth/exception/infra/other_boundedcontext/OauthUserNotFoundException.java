package hwicode.schedule.auth.exception.infra.other_boundedcontext;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OauthUserNotFoundException extends BusinessException {

    public OauthUserNotFoundException() {
        super("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
