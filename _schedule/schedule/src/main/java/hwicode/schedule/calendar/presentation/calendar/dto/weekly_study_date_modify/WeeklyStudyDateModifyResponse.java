package hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WeeklyStudyDateModifyResponse {

    private Long calendarId;
    private int weeklyStudyDate;
}
