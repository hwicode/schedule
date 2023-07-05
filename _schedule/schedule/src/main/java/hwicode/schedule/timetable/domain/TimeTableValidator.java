package hwicode.schedule.timetable.domain;

import hwicode.schedule.timetable.exception.domain.timetablevalidator.ContainOtherTimeException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.InvalidDateValidException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class TimeTableValidator {

    private LocalDate today;

    public TimeTableValidator(LocalDate today) {
        this.today = today;
    }

    public void validateStartTime(List<LearningTime> learningTimes, LocalDateTime startTime) {
        validateDate(startTime);
        validateBetweenTime(learningTimes, startTime);
        validateSameStartTime(learningTimes, startTime);
    }

    public void validateEndTime(List<LearningTime> learningTimes, LocalDateTime endTime) {
        validateDate(endTime);
        validateBetweenTime(learningTimes, endTime);
    }

    private void validateDate(LocalDateTime time) {
        LocalDate date = time.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        if (today.isEqual(date) || tomorrow.isEqual(date)) {
            return;
        }
        throw new InvalidDateValidException();
    }

    private void validateBetweenTime(List<LearningTime> learningTimes, LocalDateTime time) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isContain(time));

        if (duplication) {
            throw new ContainOtherTimeException();
        }
    }

    private void validateSameStartTime(List<LearningTime> learningTimes, LocalDateTime startTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSame(startTime));

        if (duplication) {
            throw new StartTimeDuplicateException();
        }
    }

}