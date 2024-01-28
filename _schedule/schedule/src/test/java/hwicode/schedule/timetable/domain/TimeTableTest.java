package hwicode.schedule.timetable.domain;

import hwicode.schedule.timetable.TimeTableDataHelper;
import hwicode.schedule.timetable.exception.LearningTimeNotFoundException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.ContainOtherTimeException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.InvalidDateValidException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeTableTest {

    @Test
    void 학습_시간을_생성할_때_시작_시간에_중복이_없으면_학습_시간은_생성된다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        // when
        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(TimeTableDataHelper.START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간을_생성할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(TimeTableDataHelper.START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_중복이_없으면_시작_시간이_변경된다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);

        // when
        timeTable.changeLearningTimeStartTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.NEW_START_TIME);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(TimeTableDataHelper.NEW_START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTable.createLearningTime(TimeTableDataHelper.NEW_START_TIME);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeStartTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.NEW_START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간이_다른_학습_시간과_겹치면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTable.changeLearningTimeEndTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(60));

        // when then
        LocalDateTime newTime = TimeTableDataHelper.START_TIME.plusMinutes(30);
        assertThatThrownBy(() -> timeTable.createLearningTime(newTime))
                .isInstanceOf(ContainOtherTimeException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간을_변경할_때_시작_시간이_다른_학습_시간에_포함되면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        LocalDateTime startTime = TimeTableDataHelper.START_TIME.plusMinutes(15);
        timeTable.createLearningTime(startTime);

        timeTable.changeLearningTimeEndTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(30));

        // when then
        LocalDateTime endTime = TimeTableDataHelper.START_TIME.plusMinutes(40);
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(startTime, endTime))
                .isInstanceOf(ContainOtherTimeException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간을_변경할_때_학습_시간이_다른_학습_시간을_포함하면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        LocalDateTime startTime = TimeTableDataHelper.START_TIME.plusMinutes(15);
        timeTable.createLearningTime(startTime);

        timeTable.changeLearningTimeEndTime(startTime, TimeTableDataHelper.START_TIME.plusMinutes(30));

        // when then
        LocalDateTime endTime = TimeTableDataHelper.START_TIME.plusMinutes(40);
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(TimeTableDataHelper.START_TIME, endTime))
                .isInstanceOf(ContainOtherTimeException.class);
    }

    @Test
    void 타임_테이블의_날짜보다_이전의_날짜를_가진_학습_시간을_추가하면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        LocalDateTime beforeStartTime = TimeTableDataHelper.START_TIME.minusDays(3);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(beforeStartTime))
                .isInstanceOf(InvalidDateValidException.class);
    }

    @Test
    void 타임_테이블의_날짜보다_2일_이후의_날짜를_가진_학습_시간을_추가하면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        LocalDateTime afterStartTime = TimeTableDataHelper.START_TIME.plusDays(3);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(afterStartTime))
                .isInstanceOf(InvalidDateValidException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간을_변경할_수_있다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);

        // when
        LocalDateTime changedEndTime = timeTable.changeLearningTimeEndTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.END_TIME);

        // then
        assertThat(changedEndTime).isEqualTo(TimeTableDataHelper.END_TIME);
    }

    @Test
    void 시작_시간에_해당하는_학습_시간이_없으면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.END_TIME))
                .isInstanceOf(LearningTimeNotFoundException.class);
    }

    @Test
    void 학습_시간을_삭제할_수_있다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);

        // when
        timeTable.deleteLearningTime(TimeTableDataHelper.START_TIME);

        // then
        assertThatThrownBy(() -> timeTable.deleteLearningTime(TimeTableDataHelper.START_TIME))
                .isInstanceOf(LearningTimeNotFoundException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간은_0이_된다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isZero();
    }

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간에_포함되지_않는다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTable.changeLearningTimeEndTime(TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(50));
        timeTable.createLearningTime(TimeTableDataHelper.NEW_START_TIME);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(50);
    }

    @Test
    void 타임_테이블은_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        addLearningTime(timeTable, TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(40L));
        addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(45L), TimeTableDataHelper.START_TIME.plusMinutes(80L));
        addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(90L), TimeTableDataHelper.START_TIME.plusMinutes(120L));
        addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(140L), TimeTableDataHelper.START_TIME.plusMinutes(200L));

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(165);
    }

    @Test
    void 같은_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        LearningTime learningTime1 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(40), TimeTableDataHelper.START_TIME.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(100), TimeTableDataHelper.START_TIME.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(140), TimeTableDataHelper.START_TIME.plusMinutes(230));

        learningTime1.changeSubject(TimeTableDataHelper.SUBJECT);
        learningTime2.changeSubject(TimeTableDataHelper.SUBJECT);
        learningTime3.changeSubjectOfSubTask(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));
        learningTime4.changeSubjectOfTask(new SubjectOfTask(TimeTableDataHelper.SUBJECT));

        // when
        int totalLearningTime = timeTable.getSubjectTotalLearningTime(TimeTableDataHelper.SUBJECT);

        // then
        assertThat(totalLearningTime).isEqualTo(70);
    }

    @Test
    void 같은_Task_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        LearningTime learningTime1 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(40), TimeTableDataHelper.START_TIME.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(100), TimeTableDataHelper.START_TIME.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(140), TimeTableDataHelper.START_TIME.plusMinutes(230));

        learningTime1.changeSubjectOfTask(new SubjectOfTask(TimeTableDataHelper.SUBJECT));
        learningTime2.changeSubject(TimeTableDataHelper.SUBJECT);
        learningTime3.changeSubjectOfSubTask(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));
        learningTime4.changeSubjectOfTask(new SubjectOfTask(TimeTableDataHelper.SUBJECT));

        // when
        int totalLearningTime = timeTable.getSubjectOfTaskTotalLearningTime(new SubjectOfTask(TimeTableDataHelper.SUBJECT));

        // then
        assertThat(totalLearningTime).isEqualTo(120);
    }

    @Test
    void 같은_SubTask_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate startTimeDate = TimeTableDataHelper.START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate, 1L);

        LearningTime learningTime1 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME, TimeTableDataHelper.START_TIME.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(40), TimeTableDataHelper.START_TIME.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(100), TimeTableDataHelper.START_TIME.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, TimeTableDataHelper.START_TIME.plusMinutes(140), TimeTableDataHelper.START_TIME.plusMinutes(230));

        learningTime1.changeSubjectOfSubTask(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));
        learningTime2.changeSubject(TimeTableDataHelper.SUBJECT);
        learningTime3.changeSubjectOfSubTask(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));
        learningTime4.changeSubjectOfSubTask(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));

        // when
        int totalLearningTime = timeTable.getSubjectOfSubTaskTotalLearningTime(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));

        // then
        assertThat(totalLearningTime).isEqualTo(150);
    }

    private LearningTime addLearningTime(TimeTable timeTable, LocalDateTime startTime, LocalDateTime endTime) {
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        return learningTime;
    }
}
