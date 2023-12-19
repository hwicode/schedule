package hwicode.schedule.timetable.domain;

import hwicode.schedule.timetable.exception.domain.timetablevalidator.ContainOtherTimeException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.EndTimeDuplicateException;
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
        validateBetweenTime(learningTimes, startTime, startTime);
        validateSameStartTime(learningTimes, startTime);
    }

    public void validateEndTime(List<LearningTime> learningTimes, LocalDateTime startTime, LocalDateTime endTime) {
        validateDate(endTime);
        validateBetweenTime(learningTimes, startTime, startTime);
        validateBetweenTime(learningTimes, startTime, endTime);
        validateContainedLearningTime(learningTimes, startTime, endTime);
        validateSameEndTime(learningTimes, endTime);
    }

    private void validateDate(LocalDateTime time) {
        LocalDate date = time.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        if (today.isEqual(date) || tomorrow.isEqual(date)) {
            return;
        }
        throw new InvalidDateValidException();
    }

    private void validateBetweenTime(List<LearningTime> learningTimes, LocalDateTime startTime, LocalDateTime time) {
        boolean duplication = learningTimes.stream()
                .filter(learningTime -> !learningTime.isSame(startTime))
                .anyMatch(learningTime -> learningTime.isContain(time));

        if (duplication) {
            throw new ContainOtherTimeException();
        }
    }

    private void validateContainedLearningTime(List<LearningTime> learningTimes, LocalDateTime startTime, LocalDateTime endTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isContained(startTime, endTime));

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

    private void validateSameEndTime(List<LearningTime> learningTimes, LocalDateTime endTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSameEndTime(endTime));

        if (duplication) {
            throw new EndTimeDuplicateException();
        }
    }

}
