package hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EndTimeModifyResponse {

    private LocalDateTime endTime;
}
