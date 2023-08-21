package hwicode.schedule.calendar.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class CalendarQueryResponse {

    private final Long id;
    private final YearMonth yearAndMonth;
    private final int weeklyStudyDate;

    private List<GoalQueryResponse> goalResponses;

    public CalendarQueryResponse(Long id, YearMonth yearAndMonth, int weeklyStudyDate) {
        this.id = id;
        this.yearAndMonth = yearAndMonth;
        this.weeklyStudyDate = weeklyStudyDate;
    }
}
