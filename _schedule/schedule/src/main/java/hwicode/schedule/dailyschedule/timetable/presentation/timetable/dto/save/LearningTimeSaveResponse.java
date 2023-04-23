package hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LearningTimeSaveResponse {

    @NotNull @Positive
    private Long learningTimeId;

    @NotNull
    private LocalDateTime startTime;
}
