package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
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
}
