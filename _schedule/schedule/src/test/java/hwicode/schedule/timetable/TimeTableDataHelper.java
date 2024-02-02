package hwicode.schedule.timetable;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.token.TokenProvider;

import java.time.LocalDateTime;

public class TimeTableDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long TIME_TABLE_ID = 1L;
    public static final Long LEARNING_TIME_ID = 1L;
    public static final Long SUBJECT_OF_TASK_ID = 1L;
    public static final Long SUBJECT_OF_SUBTASK_ID = 1L;


    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 4, 19, 5, 5);
    public static final LocalDateTime NEW_START_TIME = LocalDateTime.of(2023, 4, 19, 6, 6);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 4, 19, 6, 6);

    public static final String SUBJECT = "subject";
    public static final String NEW_SUBJECT = "newSubject";

    public static String createAccessToken(TokenProvider tokenProvider, Long userId) {
        OauthUser oauthUser = new OauthUser(userId, null, null, null);
        return tokenProvider.createAccessToken(oauthUser);
    }
}
