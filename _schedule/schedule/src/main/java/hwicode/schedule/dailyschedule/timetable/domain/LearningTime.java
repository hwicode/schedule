package hwicode.schedule.dailyschedule.timetable.domain;

import java.time.LocalTime;

public class LearningTime {

    private LocalTime startTime;
    private LocalTime endTime;
    private String subject;
    private SubjectOfTask subjectOfTask;
    private SubjectOfSubTask subjectOfSubTask;

    LearningTime(LocalTime startTime) {
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

    boolean isSame(LocalTime startTime) {
        return this.startTime.equals(startTime);
    }

    LocalTime changeStartTime(LocalTime newStartTime) {
        this.startTime = newStartTime;
        return this.startTime;
    }

    LocalTime changeEndTime(LocalTime endTime) {
        validateEndTime(endTime);
        this.endTime = endTime;
        return this.endTime;
    }

    private void validateEndTime(LocalTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException();
        }
    }
}
