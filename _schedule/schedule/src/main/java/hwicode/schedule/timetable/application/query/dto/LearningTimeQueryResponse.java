package hwicode.schedule.timetable.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class LearningTimeQueryResponse {

    private final Long id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String subject;
    private final Long taskId;
    private final Long subTaskId;
}
