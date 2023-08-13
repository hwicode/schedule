package hwicode.schedule.tag.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TagNotFoundException extends BusinessException {

    public TagNotFoundException() {
        super("태그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
