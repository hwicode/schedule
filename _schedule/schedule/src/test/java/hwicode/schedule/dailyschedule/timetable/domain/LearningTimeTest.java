package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LearningTimeTest {

    @Test
    void 학습_시간의_학습_주제를_삭제할_수_있다() {
        // given
        LearningTime learningTime = new LearningTime("학습 주제");

        // when
        learningTime.deleteSubject();

        // then
        assertThat(learningTime.deleteSubject()).isFalse();
    }

}

class LearningTime {

    private String subject;

    LearningTime(String subject) {
        this.subject = subject;
    }

    public boolean deleteSubject() {
        if (this.subject == null) {
            return false;
        }
        this.subject = null;
        return true;
    }
}
