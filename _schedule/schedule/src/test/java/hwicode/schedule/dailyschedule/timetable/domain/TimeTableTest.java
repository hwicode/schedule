package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeTableTest {

    @Test
    void 학습_시간을_생성할_때_시작_시간에_중복이_없으면_학습_시간은_생성된다() {
        // given
        TimeTable timeTable = new TimeTable();
        LocalTime startTime = LocalTime.of(5, 5);

        // when
        timeTable.createLearningTime(startTime);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간을_생성할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        TimeTable timeTable = new TimeTable();
        LocalTime startTime = LocalTime.of(5, 5);
        timeTable.createLearningTime(startTime);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(startTime))
                .isInstanceOf(IllegalArgumentException.class);

    }
}

class TimeTable {

    private final List<LearningTime> learningTimes = new ArrayList<>();

    public LearningTime createLearningTime(LocalTime startTime) {
        validateLearningTime(startTime);

        LearningTime learningTime = new LearningTime(startTime);
        learningTimes.add(learningTime);

        return learningTime;
    }

    private void validateLearningTime(LocalTime startTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSame(startTime));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

}
