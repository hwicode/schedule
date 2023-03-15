package hwicode.schedule.dailyschedule.checklist.exception.exception_handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private final LocalDateTime createdTime = LocalDateTime.now();
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @Getter
    public static class ValidationError {

        private final String field;
        private final String message;

        public ValidationError(final FieldError fieldError) {
            this.field = fieldError.getField();
            this.message = fieldError.getDefaultMessage();
        }
    }
}
