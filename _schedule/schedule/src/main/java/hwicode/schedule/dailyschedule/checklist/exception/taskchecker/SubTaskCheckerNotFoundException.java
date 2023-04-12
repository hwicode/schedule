package hwicode.schedule.dailyschedule.checklist.exception.taskchecker;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNotFoundException extends ChecklistBusinessException {

    public SubTaskCheckerNotFoundException() {
        super("서브 과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
