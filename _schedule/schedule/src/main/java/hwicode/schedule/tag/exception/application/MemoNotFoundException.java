package hwicode.schedule.tag.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MemoNotFoundException extends BusinessException {

    public MemoNotFoundException() {
        super("메모를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
