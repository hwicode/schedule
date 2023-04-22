package hwicode.schedule.dailyschedule.timetable.presentation;

import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyResponse;
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
}
