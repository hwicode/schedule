package hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service;

public class DailyChecklistNotFoundException extends RuntimeException {

    public DailyChecklistNotFoundException() {
        super("체크 리스트가 존재하지 않습니다.");
    }
}
