package hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class StartTimeDuplicateException extends BusinessException {

    public StartTimeDuplicateException() {
        super("시작 시간이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
