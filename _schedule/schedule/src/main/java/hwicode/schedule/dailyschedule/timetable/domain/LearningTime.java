package hwicode.schedule.dailyschedule.timetable.domain;

import hwicode.schedule.dailyschedule.timetable.exception.domain.learningtime.EndTimeNotValidException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LearningTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_to_do_list_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TimeTable timeTable;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String subject;

    @JoinColumn(name = "task_id")
    @OneToOne(fetch = FetchType.LAZY)
    private SubjectOfTask subjectOfTask;

    @JoinColumn(name = "sub_task_id")
    @OneToOne(fetch = FetchType.LAZY)
    private SubjectOfSubTask subjectOfSubTask;

    LearningTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    LearningTime(TimeTable timeTable, LocalDateTime startTime) {
        this.timeTable = timeTable;
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
            throw new EndTimeNotValidException();
        }
    }

    boolean isEndTimeNotNull() {
        return endTime != null;
    }

    int getTime() {
        Duration duration = Duration.between(startTime, endTime);
        return (int) duration.toMinutes();
    }

    boolean isSameSubject(String subject) {
        if(this.subject == null) {
            return false;
        }
        return this.subject.equals(subject);
    }

    boolean isSameSubjectOfTask(SubjectOfTask subjectOfTask) {
        if (this.subjectOfTask == null) {
            return false;
        }
        return this.subjectOfTask.getName()
                .equals(subjectOfTask.getName());
    }

    boolean isSameSubjectOfSubTask(SubjectOfSubTask subjectOfSubTask) {
        if (this.subjectOfSubTask == null) {
            return false;
        }
        return this.subjectOfSubTask.getName()
                .equals(subjectOfSubTask.getName());
    }

    public Long getId() {
        return id;
    }
}
