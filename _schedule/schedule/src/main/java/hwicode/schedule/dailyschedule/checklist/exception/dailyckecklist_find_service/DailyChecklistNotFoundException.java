package hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import org.springframework.http.HttpStatus;

public class DailyChecklistNotFoundException extends ChecklistBusinessException {

    public DailyChecklistNotFoundException() {
        super("체크 리스트가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
