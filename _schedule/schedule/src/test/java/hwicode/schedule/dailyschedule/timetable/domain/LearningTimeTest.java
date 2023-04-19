package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LearningTimeTest {

    private final String SUBJECT = "학습 주제";
    private final String NEW_SUBJECT = "새로운 학습 주제";

    @Test
    void 학습_시간의_학습_주제가_삭제되면_true가_리턴된다() {
        // given
        LearningTime learningTime = new LearningTime(SUBJECT);

        // when then
        assertThat(learningTime.deleteSubject()).isTrue();
    }

    @Test
    void 학습_시간에_학습_주제가_없으면_삭제된게_없으므로_false가_리턴된다() {
        // given
        LearningTime learningTime = new LearningTime(SUBJECT);

        // when
        learningTime.deleteSubject();

        // then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

    @Test
    void 학습_시간에서_Task_학습_주제가_삭제되면_true가_리턴된다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfTask(SUBJECT));

        // when then
        assertThat(learningTime.deleteSubject()).isTrue();
    }

    @Test
    void 학습_시간에_Task_학습_주제가_없으면_삭제된게_없으므로_false가_리턴된다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfTask(SUBJECT));

        // when
        learningTime.deleteSubject();

        // then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

    @Test
    void 학습_시간에서_SubTask_학습_주제가_삭제되면_true가_리턴된다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfSubTask(SUBJECT));

        // when then
        assertThat(learningTime.deleteSubject()).isTrue();
    }

    @Test
    void 학습_시간에_SubTask_학습_주제가_없으면_삭제된게_없으므로_false가_리턴된다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfSubTask(SUBJECT));

        // when
        learningTime.deleteSubject();

        // then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

    @Test
    void 학습_시간의_학습_주제를_수정할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(SUBJECT);

        // when
        String changedSubject = learningTime.changeSubject(NEW_SUBJECT);

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubject(NEW_SUBJECT);

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfSubTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubject(NEW_SUBJECT);

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_학습_주제를_Task_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(SUBJECT);

        // when
        String changedSubject = learningTime.changeSubjectOfTask(new SubjectOfTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_Task_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubjectOfTask(new SubjectOfTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_Task_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime(new SubjectOfSubTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubjectOfTask(new SubjectOfTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }
}

class LearningTime {

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
}

class SubjectOfTask {

    private String name;

    SubjectOfTask(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class SubjectOfSubTask {

    private String name;

    SubjectOfSubTask(String name) {
        this.name = name;
    }
}
