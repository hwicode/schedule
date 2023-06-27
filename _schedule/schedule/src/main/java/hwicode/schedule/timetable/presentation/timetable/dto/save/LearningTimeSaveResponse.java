package hwicode.schedule.timetable.presentation.timetable.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LearningTimeSaveResponse {

    private Long learningTimeId;
    private LocalDateTime startTime;
}
