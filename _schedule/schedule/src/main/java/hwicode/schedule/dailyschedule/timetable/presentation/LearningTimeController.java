package hwicode.schedule.dailyschedule.timetable.presentation;

import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
