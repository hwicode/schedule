package hwicode.schedule.dailyschedule.review;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.token.TokenProvider;

import java.time.LocalDate;

public class ReviewDataHelper {

    public static final String REVIEW_TASK_NAME = "reviewTaskName";
    public static final String REVIEW_SUB_TASK_NAME = "reviewSubTaskName";

    public static final String REVIEW_CYCLE_NAME = "reviewCycleName";
    public static final String NEW_REVIEW_CYCLE_NAME = "newReviewCycleName";

    public static final Long REVIEW_TASK_ID = 1L;
    public static final Long REVIEW_CYCLE_ID = 2L;

    public static final LocalDate START_DATE = LocalDate.of(2023, 6, 26);

    public static String createAccessToken(TokenProvider tokenProvider, Long userId) {
        OauthUser oauthUser = new OauthUser(userId, null, null, null);
        return tokenProvider.createAccessToken(oauthUser);
    }
}
