package hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyStudyDateModifyResponse {

    private Long calendarId;
    private int weeklyStudyDate;
}
