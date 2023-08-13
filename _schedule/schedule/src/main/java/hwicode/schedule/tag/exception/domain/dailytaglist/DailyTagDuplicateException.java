package hwicode.schedule.tag.exception.domain.dailytaglist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyTagDuplicateException extends BusinessException {

    public DailyTagDuplicateException() {
        super("계획표에 태그가 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
