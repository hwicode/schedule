package hwicode.schedule.dailyschedule.daily_schedule_query.exception;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyScheduleNotExistException extends BusinessException {

    public DailyScheduleNotExistException() {
        super("계획표가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
