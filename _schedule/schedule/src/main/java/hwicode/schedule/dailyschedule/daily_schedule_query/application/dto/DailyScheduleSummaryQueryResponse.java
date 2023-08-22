package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.*;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class DailyScheduleSummaryQueryResponse {

    private Long id;
    private LocalDate yearAndMonthAndDay;
    private int totalDifficultyScore;
    private int todayDonePercent;
    private Emoji emoji;
    private String mainTagName;
}
