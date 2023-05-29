package hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.YearMonth;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WeeklyStudyDateModifyRequest {

    @NotNull
    private YearMonth yearMonth;

    @NotNull
    @PositiveOrZero @Max(7)
    private Integer weeklyStudyDate;
}
