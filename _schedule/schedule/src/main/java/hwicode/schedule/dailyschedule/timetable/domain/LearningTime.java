package hwicode.schedule.dailyschedule.timetable.domain;

public class LearningTime {

    private String subject;
    private SubjectOfTask subjectOfTask;
    private SubjectOfSubTask subjectOfSubTask;

    LearningTime(String subject) {
        this.subject = subject;
    }

    LearningTime(SubjectOfTask subjectOfTask) {
        this.subjectOfTask = subjectOfTask;
    }

    LearningTime(SubjectOfSubTask subjectOfSubTask) {
        this.subjectOfSubTask = subjectOfSubTask;
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
}
