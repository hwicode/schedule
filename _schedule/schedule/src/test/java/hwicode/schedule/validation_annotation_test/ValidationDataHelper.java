package hwicode.schedule.validation_annotation_test;

public class ValidationDataHelper {

    public static final String NOT_NULL_ERROR_MESSAGE = "must not be null";
    public static final String NOT_EMPTY_ERROR_MESSAGE = "must not be empty";
    public static final String NOT_BLANK_ERROR_MESSAGE = "must not be blank";
    public static final String POSITIVE_ERROR_MESSAGE = "must be greater than 0";
    public static final String BODY_TYPE_ERROR_MESSAGE = "HTTP message body 중에 타입을 잘못 설정한게 있습니다.";

    public static String getNumberFormatExceptionMessage(String wrongInput) {
        String message = "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"%s\"";
        return String.format(message, wrongInput);
    }
}
