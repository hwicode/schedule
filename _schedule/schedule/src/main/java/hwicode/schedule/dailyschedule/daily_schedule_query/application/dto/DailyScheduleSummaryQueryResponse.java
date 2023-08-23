package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import lombok.*;

import java.time.LocalDate;

@Builder
@Data
public class DailyScheduleSummaryQueryResponse {

    private final Long id;
    private final LocalDate yearAndMonthAndDay;
    private final int totalDifficultyScore;
    private final int todayDonePercent;
    private final Emoji emoji;
    private final String mainTagName;
}
