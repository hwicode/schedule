package hwicode.schedule.dailyschedule.timetable.presentation;

import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
class LearningTimeController {

    private final LearningTimeService learningTimeService;

    @DeleteMapping("/dailyschedule/timetable/{learningTimeId}/subject")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable @NotBlank Long learningTimeId) {
        learningTimeService.deleteSubject(learningTimeId);
    }

    @PatchMapping("/dailyschedule/timetable/{learningTimeId}/subject")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectModifyResponse changeSubject(@PathVariable @NotBlank Long learningTimeId,
                                                           @RequestBody @Valid LearningTimeSubjectModifyRequest learningTimeSubjectModifyRequest) {
        String newSubject = learningTimeService.changeSubject(
                learningTimeId, learningTimeSubjectModifyRequest.getNewSubject()
        );

        return new LearningTimeSubjectModifyResponse(learningTimeId, newSubject);
    }

    @PatchMapping("/dailyschedule/timetable/{learningTimeId}/subjectoftask")
    @ResponseStatus(value = HttpStatus.OK)
    public LearningTimeSubjectOfTaskModifyResponse changeTaskOfSubject(@PathVariable @NotBlank Long learningTimeId,
                                                                 @RequestBody @Valid LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest) {
        String newSubject = learningTimeService.changeSubjectOfTask(
                learningTimeId, learningTimeSubjectOfTaskModifyRequest.getSubjectOfTaskId()
        );

        return new LearningTimeSubjectOfTaskModifyResponse(learningTimeId, newSubject);
    }
}
