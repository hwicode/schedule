package hwicode.schedule.dailyschedule.checklist;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.token.TokenProvider;

public class ChecklistDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long DAILY_CHECKLIST_ID = 1L;
    public static final Long TASK_CHECKER_ID = 2L;
    public static final Long SUB_TASK_CHECKER_ID = 3L;


    // 생성 메서드에서 사용됨. 테스트 중간에 사용되면 이미 존재하는 TaskChecker나 SubTaskChecker임
    public static final String TASK_CHECKER_NAME = "taskCheckerName";
    public static final String TASK_CHECKER_NAME2 = "taskCheckerName2";
    public static final String TASK_CHECKER_NAME3 = "taskCheckerName3";

    public static final String SUB_TASK_CHECKER_NAME = "subTaskCheckerName";
    public static final String SUB_TASK_CHECKER_NAME2 = "subTaskCheckerName2";


    // 생성 메서드를 제외하고, TaskChecker나 SubTaskChecker를 생성해야 할 때만 사용
    public static final String NEW_TASK_CHECKER_NAME = "newTaskCheckerName";
    public static final String NEW_SUB_TASK_CHECKER_NAME = "newSubTaskCheckerName";

    public static String createAccessToken(TokenProvider tokenProvider, Long userId) {
        OauthUser oauthUser = new OauthUser(userId, null, null, null);
        return tokenProvider.createAccessToken(oauthUser);
    }
}
