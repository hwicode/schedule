package hwicode.schedule.dailyschedule.checklist.exception.exception_handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않는 파라미터가 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
