package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LearningTimeTest {

    private final String SUBJECT = "학습 주제";
    private final String NEW_SUBJECT = "새로운 학습 주제";

    private LearningTime createLearningTime() {
        return new LearningTime(LocalDateTime.now());
    }

    @Test
    void 학습_시간의_학습_주제가_삭제되면_true가_리턴된다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubject(SUBJECT);

        // when then
        assertThat(learningTime.deleteSubject()).isTrue();
    }

    @Test
    void 학습_시간에서_Task_학습_주제가_삭제되면_true가_리턴된다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfTask(new SubjectOfTask(SUBJECT));

        // when then
        assertThat(learningTime.deleteSubject()).isTrue();
    }

    @Test
    void 학습_시간에서_SubTask_학습_주제가_삭제되면_true가_리턴된다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));

        // when then
        assertThat(learningTime.deleteSubject()).isTrue();
    }

    @Test
    void 학습_시간에_학습_주제가_없으면_삭제된게_없으므로_false가_리턴된다() {
        // given
        LearningTime learningTime = createLearningTime();

        // when then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

    @Test
    void 학습_시간의_학습_주제를_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubject(SUBJECT);

        // when
        String changedSubject = learningTime.changeSubject(NEW_SUBJECT);

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfTask(new SubjectOfTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubject(NEW_SUBJECT);

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubject(NEW_SUBJECT);

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_학습_주제를_Task_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubject(SUBJECT);

        // when
        String changedSubject = learningTime.changeSubjectOfTask(new SubjectOfTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_Task_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfTask(new SubjectOfTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubjectOfTask(new SubjectOfTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_Task_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubjectOfTask(new SubjectOfTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_학습_주제를_SubTask_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubject(SUBJECT);

        // when
        String changedSubject = learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_SubTask_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfTask(new SubjectOfTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_SubTask_학습_주제로_수정할_수_있다() {
        // given
        LearningTime learningTime = createLearningTime();
        learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));

        // when
        String changedSubject = learningTime.changeSubjectOfSubTask(new SubjectOfSubTask(NEW_SUBJECT));

        // then
        assertThat(changedSubject).isEqualTo(NEW_SUBJECT);
    }
}

