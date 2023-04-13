package hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNameDuplicationException extends BusinessException {

    public SubTaskCheckerNameDuplicationException() {
        super("서브 과제 체커의 이름이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
