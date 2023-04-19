package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_false가_리턴한다() {
        // given
        LearningTime learningTime = createLearningTime();

        // when
        boolean isNotNull = learningTime.isEndTimeNotNull();

        // then
        assertThat(isNotNull).isFalse();
    }

    @ParameterizedTest
    @MethodSource("provideLocalDateTimesAndTotalTime")
    void 학습_시간의_총_시간을_가져올_수_있다(LocalDateTime startTime, LocalDateTime endTime, int totalTime) {
        // given
        LearningTime learningTime = new LearningTime(startTime);

        //when
        learningTime.changeEndTime(endTime);

        // then
        assertThat(learningTime.getTime()).isEqualTo(totalTime);
    }

    private static Stream<Arguments> provideLocalDateTimesAndTotalTime() {
        return Stream.of(
                arguments(
                        LocalDateTime.of(2023, 4, 19, 1, 1),
                        LocalDateTime.of(2023, 4, 19, 1, 1),
                        0
                ),
                arguments(
                        LocalDateTime.of(2023, 4, 19, 1, 1),
                        LocalDateTime.of(2023, 4, 19, 5, 1),
                        240
                ),
                arguments(
                        LocalDateTime.of(2023, 4, 19, 19, 18),
                        LocalDateTime.of(2023, 4, 19, 21, 42),
                        144
                ),
                arguments(
                        LocalDateTime.of(2023, 4, 19, 3, 3),
                        LocalDateTime.of(2023, 4, 19, 5, 5),
                        122
                ),
                arguments(
                        LocalDateTime.of(2023, 4, 19, 23, 1),
                        LocalDateTime.of(2023, 4, 20, 0, 51),
                        110
                )
        );
    }

}

