package hwicode.schedule.dailyschedule.daily_schedule_query;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.token.TokenProvider;

public class DailyScheduleQueryDataHelper {

    public static String createAccessToken(TokenProvider tokenProvider, Long userId) {
        OauthUser oauthUser = new OauthUser(userId, null, null, null);
        return tokenProvider.createAccessToken(oauthUser);
    }
}
