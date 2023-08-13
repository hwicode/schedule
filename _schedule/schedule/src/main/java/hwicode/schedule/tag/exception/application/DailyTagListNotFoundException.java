package hwicode.schedule.tag.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyTagListNotFoundException extends BusinessException {

    public DailyTagListNotFoundException() {
        super("계획표의 태그들을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
