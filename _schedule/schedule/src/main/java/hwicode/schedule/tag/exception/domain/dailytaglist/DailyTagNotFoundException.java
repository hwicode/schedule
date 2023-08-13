package hwicode.schedule.tag.exception.domain.dailytaglist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyTagNotFoundException extends BusinessException {

    public DailyTagNotFoundException() {
        super("계획표의 태그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
