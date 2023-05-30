package hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EndTimeModifyResponse {

    private LocalDateTime endTime;
}
