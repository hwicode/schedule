package hwicode.schedule.dailyschedule.todolist;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.token.TokenProvider;

public class ToDoListDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long DAILY_TO_DO_LIST_ID = 1L;
    public static final Long TASK_ID = 2L;
    public static final Long SUB_TASK_ID = 3L;

    // given절에서 사용됨, 테스트 중간에 사용되면 이미 존재하는 Task나 SubTask임
    public static final String TASK_NAME = "taskName";
    public static final String SUB_TASK_NAME = "subTaskName";

    public static String createAccessToken(TokenProvider tokenProvider, Long userId) {
        OauthUser oauthUser = new OauthUser(userId, null, null, null);
        return tokenProvider.createAccessToken(oauthUser);
    }
}
