package hwicode.schedule.dailyschedule.timetable.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeTable {

    private LocalDate today;
    private final List<LearningTime> learningTimes = new ArrayList<>();

    public TimeTable(LocalDate today) {
        this.today = today;
    }

    public LearningTime createLearningTime(LocalDateTime startTime) {
        validateStartTime(startTime);

        LearningTime learningTime = new LearningTime(startTime);
        learningTimes.add(learningTime);

        return learningTime;
    }

    public LocalDateTime changeLearningTimeStartTime(LocalDateTime startTime, LocalDateTime newStartTime) {
        validateStartTime(newStartTime);
        return findLearningTimeBy(startTime).changeStartTime(newStartTime);
    }

    private void validateStartTime(LocalDateTime startTime) {
        validateDate(startTime);
        validateBetweenTime(startTime);
        validateSameStartTime(startTime);
    }

    private void validateSameStartTime(LocalDateTime startTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSame(startTime));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public LocalDateTime changeLearningTimeEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        validateEndTime(endTime);
        return findLearningTimeBy(startTime).changeEndTime(endTime);
    }

    private void validateEndTime(LocalDateTime endTime) {
        validateDate(endTime);
        validateBetweenTime(endTime);
    }

    private void validateDate(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        if (today.isEqual(localDate) || tomorrow.isEqual(localDate)) {
            return;
        }
        throw new IllegalArgumentException();
    }

    private void validateBetweenTime(LocalDateTime time) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isContain(time));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public void deleteLearningTime(LocalDateTime startTime) {
        learningTimes.remove(findLearningTimeBy(startTime));
    }

    private LearningTime findLearningTimeBy(LocalDateTime startTime) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSame(startTime))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getTotalLearningTime() {
        return learningTimes.stream()
                .filter(LearningTime::isEndTimeNotNull)
                .mapToInt(LearningTime::getTime)
                .sum();
    }

    public int getSubjectTotalLearningTime(String subject) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSameSubject(subject))
                .filter(LearningTime::isEndTimeNotNull)
                .mapToInt(LearningTime::getTime)
                .sum();
    }

    public int getSubjectOfTaskTotalLearningTime(SubjectOfTask subjectOfTask) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSameSubjectOfTask(subjectOfTask))
                .filter(LearningTime::isEndTimeNotNull)
                .mapToInt(LearningTime::getTime)
                .sum();
    }

    public int getSubjectOfSubTaskTotalLearningTime(SubjectOfSubTask subjectOfSubTask) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSameSubjectOfSubTask(subjectOfSubTask))
                .filter(LearningTime::isEndTimeNotNull)
                .mapToInt(LearningTime::getTime)
                .sum();
    }
}
