package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.*;

import java.time.LocalDate;

@Data
public class DailyScheduleSummaryQueryResponse {

    private final Long id;
    private final LocalDate yearAndMonthAndDay;
    private final int totalDifficultyScore;
    private final int todayDonePercent;
    private final Emoji emoji;
    private final String mainTagName;

    @Builder
    public DailyScheduleSummaryQueryResponse(Long id, LocalDate yearAndMonthAndDay, int totalDifficultyScore, int todayDonePercent, Emoji emoji, String mainTagName) {
        this.id = id;
        this.yearAndMonthAndDay = yearAndMonthAndDay;
        this.totalDifficultyScore = totalDifficultyScore;
        this.todayDonePercent = todayDonePercent;
        this.emoji = emoji;
        this.mainTagName = mainTagName;
    }
}
