package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeTableTest {

    private final LocalDateTime startTime = LocalDateTime.of(2023, 4, 19, 5, 5);
    private final LocalDateTime newStartTime = LocalDateTime.of(2023, 4, 19, 6, 6);
    private final LocalDateTime endTime = LocalDateTime.of(2023, 4, 19, 6, 6);

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
    void 학습_시간이_다른_학습_시간과_겹치면_에러가_발생한다() {
        // given
        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 1, 1, 1);

        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(time);
        timeTable.changeLearningTimeEndTime(time, time.plusMinutes(60));

        // when then
        LocalDateTime newTime = time.plusMinutes(30);
        assertThatThrownBy(() -> timeTable.createLearningTime(newTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간을_변경할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);

        // when
        LocalDateTime changedEndTime = timeTable.changeLearningTimeEndTime(startTime, endTime);

        // then
        assertThat(changedEndTime).isEqualTo(endTime);
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

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간은_0이_된다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isZero();
    }

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간에_포함되지_않는다() {
        // given
        TimeTable timeTable = new TimeTable();
        timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, startTime.plusMinutes(50));
        timeTable.createLearningTime(newStartTime);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(50);
    }

    @Test
    void 타임테이블은_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 1, 1, 1);

        LocalDateTime[] startTimes = {
                localDateTime,
                localDateTime.plusMinutes(45L),
                localDateTime.plusMinutes(90L),
                localDateTime.plusMinutes(140L)
        };
        LocalDateTime[] endTimes = {
                localDateTime.plusMinutes(40L),
                localDateTime.plusMinutes(80L),
                localDateTime.plusMinutes(120L),
                localDateTime.plusMinutes(200L)
        };

        TimeTable timeTable = createTimeTableWithLearningTimes(startTimes, endTimes);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(165);
    }

    private TimeTable createTimeTableWithLearningTimes(LocalDateTime[] startTimes, LocalDateTime[] endTimes) {
        TimeTable timeTable = new TimeTable();

        for (int i = 0; i < 4; i++) {
            timeTable.createLearningTime(startTimes[i]);
            timeTable.changeLearningTimeEndTime(startTimes[i], endTimes[i]);
        }

        return timeTable;
    }

}

class TimeTable {

    private final List<LearningTime> learningTimes = new ArrayList<>();

    public LearningTime createLearningTime(LocalDateTime startTime) {
        validateStartTime(startTime);

        LearningTime learningTime = new LearningTime(startTime);
        learningTimes.add(learningTime);

        return learningTime;
    }

    public LocalDateTime changeLearningTimeStartTime(LocalDateTime startTime, LocalDateTime newStartTime) {
        validateStartTime(newStartTime);
        return findLearningTimeBy(startTime).changeStartTime(newStartTime);
    }

    private void validateStartTime(LocalDateTime startTime) {
        validateBetweenTime(startTime);
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isSame(startTime));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public LocalDateTime changeLearningTimeEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        validateBetweenTime(endTime);
        return findLearningTimeBy(startTime).changeEndTime(endTime);
    }

    private void validateBetweenTime(LocalDateTime time) {
        boolean duplication = learningTimes.stream()
                .anyMatch(learningTime -> learningTime.isContain(time));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public void deleteLearningTime(LocalDateTime startTime) {
        learningTimes.remove(findLearningTimeBy(startTime));
    }

    private LearningTime findLearningTimeBy(LocalDateTime startTime) {
        return learningTimes.stream()
                .filter(learningTime -> learningTime.isSame(startTime))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getTotalLearningTime() {
        return learningTimes.stream()
                .filter(LearningTime::isEndTimeNotNull)
                .mapToInt(LearningTime::getTime)
                .sum();
    }

}
