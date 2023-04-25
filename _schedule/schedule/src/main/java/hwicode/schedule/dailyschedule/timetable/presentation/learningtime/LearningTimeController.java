package hwicode.schedule.dailyschedule.timetable.presentation.learningtime;

import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class LearningTimeController {

    private final LearningTimeService learningTimeService;

    @DeleteMapping("/dailyschedule/learning-times/{learningTimeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable @NotBlank Long learningTimeId) {
        learningTimeService.deleteSubject(learningTimeId);
    }

    @PatchMapping("/dailyschedule/learning-times/{learningTimeId}/subject")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectModifyResponse changeSubject(@PathVariable @NotBlank Long learningTimeId,
                                                           @RequestBody @Valid LearningTimeSubjectModifyRequest learningTimeSubjectModifyRequest) {
        String newSubject = learningTimeService.changeSubject(
                learningTimeId, learningTimeSubjectModifyRequest.getNewSubject()
        );

        return new LearningTimeSubjectModifyResponse(learningTimeId, newSubject);
    }

    @PatchMapping("/dailyschedule/learning-times/{learningTimeId}/subject-of-task")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectOfTaskModifyResponse changeTaskOfSubject(@PathVariable @NotBlank Long learningTimeId,
                                                                       @RequestBody @Valid LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest) {
        String newSubject = learningTimeService.changeSubjectOfTask(
                learningTimeId, learningTimeSubjectOfTaskModifyRequest.getSubjectOfTaskId()
        );

        return new LearningTimeSubjectOfTaskModifyResponse(learningTimeId, newSubject);
    }

    @PatchMapping("/dailyschedule/learning-times/{learningTimeId}/subject-of-subtask")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectOfSubTaskModifyResponse changeSubTaskOfSubject(@PathVariable @NotBlank Long learningTimeId,
                                                                             @RequestBody @Valid LearningTimeSubjectOfSubTaskModifyRequest learningTimeSubjectOfSubTaskModifyRequest) {
        String newSubject = learningTimeService.changeSubjectOfSubTask(
                learningTimeId, learningTimeSubjectOfSubTaskModifyRequest.getSubjectOfSubTaskId()
        );

        return new LearningTimeSubjectOfSubTaskModifyResponse(learningTimeId, newSubject);
    }
}
