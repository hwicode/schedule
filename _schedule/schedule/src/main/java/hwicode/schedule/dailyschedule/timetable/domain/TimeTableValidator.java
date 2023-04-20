package hwicode.schedule.dailyschedule.timetable.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    private void validateDate(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        if (today.isEqual(localDate) || tomorrow.isEqual(localDate)) {
            return;
        }
        throw new IllegalArgumentException();
    }

    private void validateBetweenTime(List<LearningTime> learningTimes, LocalDateTime time) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isContain(time));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSameStartTime(List<LearningTime> learningTimes, LocalDateTime startTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSame(startTime));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

}
