package hwicode.schedule.dailyschedule.timetable.domain;

import java.time.Duration;
import java.time.LocalDateTime;

public class LearningTime {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String subject;
    private SubjectOfTask subjectOfTask;
    private SubjectOfSubTask subjectOfSubTask;

    LearningTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public boolean deleteSubject() {
        if (this.subject == null && subjectOfTask == null && subjectOfSubTask == null) {
            return false;
        }
        this.subject = null;
        this.subjectOfTask = null;
        this.subjectOfSubTask = null;
        return true;
    }

    public String changeSubject(String subject) {
        deleteSubject();
        this.subject = subject;
        return this.subject;
    }

    public String changeSubjectOfTask(SubjectOfTask subjectOfTask) {
        deleteSubject();
        this.subjectOfTask = subjectOfTask;
        return this.subjectOfTask.getName();
    }

    public String changeSubjectOfSubTask(SubjectOfSubTask subjectOfSubTask) {
        deleteSubject();
        this.subjectOfSubTask = subjectOfSubTask;
        return this.subjectOfSubTask.getName();
    }

    boolean isSame(LocalDateTime startTime) {
        return this.startTime.equals(startTime);
    }

    boolean isContain(LocalDateTime time) {
        if (endTime == null) {
            return false;
        }
        return startTime.isBefore(time) && endTime.isAfter(time);
    }

    LocalDateTime changeStartTime(LocalDateTime newStartTime) {
        this.startTime = newStartTime;
        return this.startTime;
    }

    LocalDateTime changeEndTime(LocalDateTime endTime) {
        validateEndTime(endTime);
        this.endTime = endTime;
        return this.endTime;
    }

    private void validateEndTime(LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException();
        }
    }

    boolean isEndTimeNotNull() {
        return endTime != null;
    }

    int getTime() {
        Duration duration = Duration.between(startTime, endTime);
        return (int) duration.toMinutes();
    }
}
