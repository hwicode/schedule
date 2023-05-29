package hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalAddToCalendarsRequest {

    @NotEmpty
    private Set<YearMonth> yearMonths;

    public List<YearMonth> getYearMonths() {
        return new ArrayList<>(yearMonths);
    }
}
