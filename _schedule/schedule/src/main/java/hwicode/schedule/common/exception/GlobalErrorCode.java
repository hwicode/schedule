package hwicode.schedule.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않는 파라미터가 있습니다."),
    INVALID_MESSAGE_BODY_TYPE(HttpStatus.BAD_REQUEST, "HTTP message body 중에 타입을 잘못 설정한게 있습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 에러가 발생하였습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 URL입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
