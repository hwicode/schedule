package hwicode.schedule.tag.exception.domain.memo;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MemoForbiddenException extends BusinessException {

    public MemoForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}
