package hwicode.schedule.calendar.presentation.calendar.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CalendarSaveRequest {

    @NotNull
    private YearMonth yearMonth;
}
