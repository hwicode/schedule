package hwicode.schedule.tag.exception.domain.memo;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidNumberOfTagsException extends BusinessException {

    public InvalidNumberOfTagsException() {
        super("한 번에 메모에 추가할 수 있는 태그의 수는 10개입니다.", HttpStatus.BAD_REQUEST);
    }
}
