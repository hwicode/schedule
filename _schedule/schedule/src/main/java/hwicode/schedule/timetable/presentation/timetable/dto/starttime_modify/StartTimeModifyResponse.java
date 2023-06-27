package hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StartTimeModifyResponse {

    private LocalDateTime newStartTime;
}
