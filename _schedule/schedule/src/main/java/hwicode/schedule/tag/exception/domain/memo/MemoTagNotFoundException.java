package hwicode.schedule.tag.exception.domain.memo;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MemoTagNotFoundException extends BusinessException {

    public MemoTagNotFoundException() {
        super("메모의 태그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
