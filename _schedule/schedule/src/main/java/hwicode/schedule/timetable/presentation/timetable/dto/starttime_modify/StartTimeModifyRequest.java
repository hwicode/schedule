package hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StartTimeModifyRequest {

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime newStartTime;
}
