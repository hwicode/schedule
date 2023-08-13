package hwicode.schedule.tag.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TagDuplicateException extends BusinessException {

    public TagDuplicateException() {
        super("태그가 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
