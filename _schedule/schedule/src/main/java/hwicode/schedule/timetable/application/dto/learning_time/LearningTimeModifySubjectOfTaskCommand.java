package hwicode.schedule.timetable.application.dto.learning_time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LearningTimeModifySubjectOfTaskCommand {

    private final Long userId;
    private final Long learningTimeId;
    private final Long subjectOfTaskId;
}
