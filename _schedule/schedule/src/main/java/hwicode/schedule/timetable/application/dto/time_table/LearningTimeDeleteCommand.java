package hwicode.schedule.timetable.application.dto.time_table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class LearningTimeDeleteCommand {

    private final Long userId;
    private final Long timeTableId;
    private final LocalDateTime startTime;
}
