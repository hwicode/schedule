package hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LearningTimeSaveResponse {

    private Long learningTimeId;
    private LocalDateTime startTime;
}
