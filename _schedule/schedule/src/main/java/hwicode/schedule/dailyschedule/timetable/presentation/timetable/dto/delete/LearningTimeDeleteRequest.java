package hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.delete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LearningTimeDeleteRequest {

    @NotNull
    private LocalDateTime startTime;
}
