package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LearningTimeTest {

    private final String subject = "학습 주제";

    @Test
    void 학습_시간의_학습_주제를_삭제할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(subject);

        // when
        learningTime.deleteSubject();

        // then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

    @Test
    void 학습_시간에서_Task_학습_주제를_삭제할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfTask(subject));

        // when
        learningTime.deleteSubject();

        // then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

}

class LearningTime {

    private String subject;
    private SubjectOfTask subjectOfTask;

    LearningTime(String subject) {
        this.subject = subject;
    }

    LearningTime(SubjectOfTask subjectOfTask) {
        this.subjectOfTask = subjectOfTask;
    }

    public boolean deleteSubject() {
        if (this.subject == null && subjectOfTask == null) {
            return false;
        }
        this.subject = null;
        this.subjectOfTask = null;
        return true;
    }
}

class SubjectOfTask {

    private String name;

    SubjectOfTask(String name) {
        this.name = name;
    }
}
