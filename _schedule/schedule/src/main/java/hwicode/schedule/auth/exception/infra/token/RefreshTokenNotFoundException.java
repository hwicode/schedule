package hwicode.schedule.auth.exception.infra.token;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends BusinessException {

    public RefreshTokenNotFoundException() {
        super("Refresh 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
