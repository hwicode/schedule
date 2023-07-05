package hwicode.schedule.timetable.domain;

import hwicode.schedule.timetable.exception.LearningTimeNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_schedule")
@Entity
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TimeTableValidator validator;

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<LearningTime> learningTimes = new ArrayList<>();

    // 테스트 코드에서만 사용되는 생성자!
    public TimeTable(LocalDate today) {
        validator = new TimeTableValidator(today);
    }

    public LearningTime createLearningTime(LocalDateTime startTime) {
        validator.validateStartTime(learningTimes, startTime);

        LearningTime learningTime = new LearningTime(this, startTime);
        learningTimes.add(learningTime);

        return learningTime;
    }

    public LocalDateTime changeLearningTimeStartTime(LocalDateTime startTime, LocalDateTime newStartTime) {
        validator.validateStartTime(learningTimes, newStartTime);
        return findLearningTimeBy(startTime).changeStartTime(newStartTime);
    }

    public LocalDateTime changeLearningTimeEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        validator.validateEndTime(learningTimes, endTime);
        return findLearningTimeBy(startTime).changeEndTime(endTime);
    }

    public void deleteLearningTime(LocalDateTime startTime) {
        learningTimes.remove(findLearningTimeBy(startTime));
    }

    private LearningTime findLearningTimeBy(LocalDateTime startTime) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSame(startTime))
                .findFirst()
                .orElseThrow(LearningTimeNotFoundException::new);
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

    public Long getId() {
        return id;
    }
}