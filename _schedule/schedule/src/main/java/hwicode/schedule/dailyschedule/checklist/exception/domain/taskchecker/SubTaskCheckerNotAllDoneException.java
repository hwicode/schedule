package hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNotAllDoneException extends BusinessException {

    public SubTaskCheckerNotAllDoneException() {
        super("서브 과제 체커가 전부 DONE 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
