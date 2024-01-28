package hwicode.schedule.timetable.presentation.learningtime;

import hwicode.schedule.timetable.application.LearningTimeAggregateService;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeDeleteSubjectCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfSubTaskCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfTaskCommand;
import hwicode.schedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyResponse;
import hwicode.schedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyRequest;
import hwicode.schedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyResponse;
import hwicode.schedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import hwicode.schedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class LearningTimeController {

    private final LearningTimeAggregateService learningTimeAggregateService;

    @DeleteMapping("/dailyschedule/learning-times/{learningTimeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable @Positive Long learningTimeId) {
        LearningTimeDeleteSubjectCommand command = new LearningTimeDeleteSubjectCommand(1L, learningTimeId);
        learningTimeAggregateService.deleteSubject(command);
    }

    @PatchMapping("/dailyschedule/learning-times/{learningTimeId}/subject")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectModifyResponse changeSubject(@PathVariable @Positive Long learningTimeId,
                                                           @RequestBody @Valid LearningTimeSubjectModifyRequest request) {
        LearningTimeModifySubjectCommand command = new LearningTimeModifySubjectCommand(
                1L, learningTimeId, request.getNewSubject()
        );
        String newSubject = learningTimeAggregateService.changeSubject(command);
        return new LearningTimeSubjectModifyResponse(learningTimeId, newSubject);
    }

    @PatchMapping("/dailyschedule/learning-times/{learningTimeId}/subject-of-task")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectOfTaskModifyResponse changeTaskOfSubject(@PathVariable @Positive Long learningTimeId,
                                                                       @RequestBody @Valid LearningTimeSubjectOfTaskModifyRequest request) {
        LearningTimeModifySubjectOfTaskCommand command = new LearningTimeModifySubjectOfTaskCommand(
                1L, learningTimeId, request.getSubjectOfTaskId()
        );
        String newSubject = learningTimeAggregateService.changeSubjectOfTask(command);
        return new LearningTimeSubjectOfTaskModifyResponse(learningTimeId, newSubject);
    }

    @PatchMapping("/dailyschedule/learning-times/{learningTimeId}/subject-of-subtask")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectOfSubTaskModifyResponse changeSubTaskOfSubject(@PathVariable @Positive Long learningTimeId,
                                                                             @RequestBody @Valid LearningTimeSubjectOfSubTaskModifyRequest request) {
        LearningTimeModifySubjectOfSubTaskCommand command = new LearningTimeModifySubjectOfSubTaskCommand(
                1L, learningTimeId, request.getSubjectOfSubTaskId()
        );
        String newSubject = learningTimeAggregateService.changeSubjectOfSubTask(command);
        return new LearningTimeSubjectOfSubTaskModifyResponse(learningTimeId, newSubject);
    }
}
