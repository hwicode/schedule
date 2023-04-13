package hwicode.schedule.dailyschedule.checklist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyChecklistNotFoundException extends BusinessException {

    public DailyChecklistNotFoundException() {
        super("체크 리스트가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
