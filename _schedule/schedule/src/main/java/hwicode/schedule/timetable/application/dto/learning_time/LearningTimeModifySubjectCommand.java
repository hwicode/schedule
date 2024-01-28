package hwicode.schedule.timetable.application.dto.learning_time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LearningTimeModifySubjectCommand {

    private final Long userId;
    private final Long learningTimeId;
    private final String subject;
}
