package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeTableTest {

    private final LocalTime startTime = LocalTime.of(5, 5);
    private final LocalTime newStartTime = LocalTime.of(6, 6);
    private final LocalTime endTime = LocalTime.of(6, 6);

    @Test
    void 학습_시간을_생성할_때_시작_시간에_중복이_없으면_학습_시간은_생성된다() {
        // given
        TimeTable timeTable = new TimeTable();

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
        timeTable.createLearningTime(startTime);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_중복이_없으면_시작_시간이_변경된다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);

        // when
        timeTable.changeLearningTimeStartTime(startTime, newStartTime);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(newStartTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeStartTime(startTime, startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간을_변경할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);

        // when
        LocalTime changedEndTime = timeTable.changeLearningTimeEndTime(startTime, endTime);

        // then
        assertThat(changedEndTime).isEqualTo(endTime);
    }

    @Test
    void 학습_시간의_끝나는_시간이_시작_시간보다_이전_시간이면_에러가_발생한다() {
        // given
        TimeTable timeTable = new TimeTable();

        LocalTime startTime = LocalTime.of(3, 3);
        LocalTime endTime = LocalTime.of(2, 2);

        timeTable.createLearningTime(startTime);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시작_시간에_해당하는_학습_시간이_없으면_에러가_발생한다() {
        // given
        TimeTable timeTable = new TimeTable();

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간을_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);

        // when
        timeTable.deleteLearningTime(startTime);

        // then
        assertThatThrownBy(() -> timeTable.deleteLearningTime(startTime))
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

    public LocalTime changeLearningTimeStartTime(LocalTime startTime, LocalTime newStartTime) {
        validateLearningTime(newStartTime);
        return findLearningTimeBy(startTime).changeStartTime(newStartTime);
    }

    private void validateLearningTime(LocalTime startTime) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSame(startTime));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public LocalTime changeLearningTimeEndTime(LocalTime startTime, LocalTime endTime) {
        return findLearningTimeBy(startTime).changeEndTime(endTime);
    }

    public void deleteLearningTime(LocalTime startTime) {
        learningTimes.remove(findLearningTimeBy(startTime));
    }

    private LearningTime findLearningTimeBy(LocalTime startTime) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSame(startTime))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
