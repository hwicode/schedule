package hwicode.schedule.timetable.exception.domain.timetablevalidator;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EndTimeDuplicateException extends BusinessException {

    public EndTimeDuplicateException() {
        super("종료 시간이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
