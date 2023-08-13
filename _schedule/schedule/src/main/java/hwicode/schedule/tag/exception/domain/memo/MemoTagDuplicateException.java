package hwicode.schedule.tag.exception.domain.memo;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MemoTagDuplicateException extends BusinessException {

    public MemoTagDuplicateException() {
        super("메모에 태그가 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
