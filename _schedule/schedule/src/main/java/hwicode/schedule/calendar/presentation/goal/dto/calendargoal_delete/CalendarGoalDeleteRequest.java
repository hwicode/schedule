package hwicode.schedule.calendar.presentation.goal.dto.calendargoal_delete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CalendarGoalDeleteRequest {

    @NotNull
    private YearMonth yearMonth;
}
