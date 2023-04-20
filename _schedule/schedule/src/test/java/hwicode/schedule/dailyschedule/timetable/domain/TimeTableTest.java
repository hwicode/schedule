package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeTableTest {

    private final LocalDateTime startTime = LocalDateTime.of(2023, 4, 19, 5, 5);
    private final LocalDateTime newStartTime = LocalDateTime.of(2023, 4, 19, 6, 6);
    private final LocalDateTime endTime = LocalDateTime.of(2023, 4, 19, 6, 6);

    private final String SUBJECT = "학습 주제";

    @Test
    void 학습_시간을_생성할_때_시작_시간에_중복이_없으면_학습_시간은_생성된다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        // when
        timeTable.createLearningTime(startTime);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간을_생성할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        timeTable.createLearningTime(startTime);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_중복이_없으면_시작_시간이_변경된다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

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
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        timeTable.createLearningTime(startTime);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeStartTime(startTime, startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간이_다른_학습_시간과_겹치면_에러가_발생한다() {
        // given
        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 1, 1, 1);

        TimeTable timeTable = new TimeTable(time.toLocalDate());
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
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        timeTable.createLearningTime(startTime);

        // when
        LocalDateTime changedEndTime = timeTable.changeLearningTimeEndTime(startTime, endTime);

        // then
        assertThat(changedEndTime).isEqualTo(endTime);
    }

    @Test
    void 타임_테이블의_날짜보다_이전의_날짜를_가진_학습_시간을_추가하면_에러가_발생한다() {
        // given
        LocalDate date = LocalDate.of(2023, 1, 1);
        TimeTable timeTable = new TimeTable(date);

        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime beforeDateTime = dateTime.minusDays(3);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(beforeDateTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 타임_테이블의_날짜보다_2일_이후의_날짜를_가진_학습_시간을_추가하면_에러가_발생한다() {
        // given
        LocalDate date = LocalDate.of(2023, 1, 1);
        TimeTable timeTable = new TimeTable(date);

        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime afterDateTime = dateTime.plusDays(3);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(afterDateTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시작_시간에_해당하는_학습_시간이_없으면_에러가_발생한다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간을_삭제할_수_있다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

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
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        timeTable.createLearningTime(startTime);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isZero();
    }

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간에_포함되지_않는다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, startTime.plusMinutes(50));
        timeTable.createLearningTime(newStartTime);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(50);
    }

    @Test
    void 타임_테이블은_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate localDate = LocalDate.of(2023, 1, 1);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);

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

        TimeTable timeTable = createTimeTableWithLearningTimes(localDate, startTimes, endTimes);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(165);
    }

    private TimeTable createTimeTableWithLearningTimes(LocalDate localDate, LocalDateTime[] startTimes, LocalDateTime[] endTimes) {
        TimeTable timeTable = new TimeTable(localDate);

        for (int i = 0; i < 4; i++) {
            timeTable.createLearningTime(startTimes[i]);
            timeTable.changeLearningTimeEndTime(startTimes[i], endTimes[i]);
        }

        return timeTable;
    }

    @Test
    void 같은_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        LearningTime learningTime1 = addLearningTime(timeTable, startTime, startTime.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, startTime.plusMinutes(40), startTime.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, startTime.plusMinutes(100), startTime.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, startTime.plusMinutes(140), startTime.plusMinutes(230));

        learningTime1.changeSubject(SUBJECT);
        learningTime2.changeSubject(SUBJECT);
        learningTime3.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));
        learningTime4.changeSubjectOfTask(new SubjectOfTask(SUBJECT));

        // when
        int totalLearningTime = timeTable.getSubjectTotalLearningTime(SUBJECT);

        // then
        assertThat(totalLearningTime).isEqualTo(70);
    }

    @Test
    void 같은_Task_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        LearningTime learningTime1 = addLearningTime(timeTable, startTime, startTime.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, startTime.plusMinutes(40), startTime.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, startTime.plusMinutes(100), startTime.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, startTime.plusMinutes(140), startTime.plusMinutes(230));

        learningTime1.changeSubjectOfTask(new SubjectOfTask(SUBJECT));
        learningTime2.changeSubject(SUBJECT);
        learningTime3.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));
        learningTime4.changeSubjectOfTask(new SubjectOfTask(SUBJECT));

        // when
        int totalLearningTime = timeTable.getSubjectOfTaskTotalLearningTime(new SubjectOfTask(SUBJECT));

        // then
        assertThat(totalLearningTime).isEqualTo(120);
    }

    @Test
    void 같은_SubTask_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate localDate = startTime.toLocalDate();
        TimeTable timeTable = new TimeTable(localDate);

        LearningTime learningTime1 = addLearningTime(timeTable, startTime, startTime.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, startTime.plusMinutes(40), startTime.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, startTime.plusMinutes(100), startTime.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, startTime.plusMinutes(140), startTime.plusMinutes(230));

        learningTime1.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));
        learningTime2.changeSubject(SUBJECT);
        learningTime3.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));
        learningTime4.changeSubjectOfSubTask(new SubjectOfSubTask(SUBJECT));

        // when
        int totalLearningTime = timeTable.getSubjectOfSubTaskTotalLearningTime(new SubjectOfSubTask(SUBJECT));

        // then
        assertThat(totalLearningTime).isEqualTo(150);
    }

    private LearningTime addLearningTime(TimeTable timeTable, LocalDateTime startTime, LocalDateTime endTime) {
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        return learningTime;
    }
}
