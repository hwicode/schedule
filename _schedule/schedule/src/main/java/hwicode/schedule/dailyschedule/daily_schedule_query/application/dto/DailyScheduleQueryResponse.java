package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyScheduleQueryResponse {

    private final Long id;
    private final LocalDate yearAndMonthAndDay;
    private final int totalDifficultyScore;
    private final int todayDonePercent;
    private final int totalLearningTime;
    private final Emoji emoji;
    private final String mainTagName;
    private final String review;

    private List<TaskQueryResponse> taskQueryResponses;

    @Builder
    public DailyScheduleQueryResponse(Long id, LocalDate yearAndMonthAndDay, int totalDifficultyScore, int todayDonePercent, int totalLearningTime, Emoji emoji, String mainTagName, String review) {
        this.id = id;
        this.yearAndMonthAndDay = yearAndMonthAndDay;
        this.totalDifficultyScore = totalDifficultyScore;
        this.todayDonePercent = todayDonePercent;
        this.totalLearningTime = totalLearningTime;
        this.emoji = emoji;
        this.mainTagName = mainTagName;
        this.review = review;
    }
}
