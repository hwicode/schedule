package hwicode.schedule.dailyschedule.timetable.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeTableTest {

    @Test
    void 학습_시간을_생성할_때_시작_시간에_중복이_없으면_학습_시간은_생성된다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        // when
        timeTable.createLearningTime(START_TIME);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(START_TIME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간을_생성할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(START_TIME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_중복이_없으면_시작_시간이_변경된다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);

        // when
        timeTable.changeLearningTimeStartTime(START_TIME, NEW_START_TIME);

        // then
        assertThatThrownBy(() -> timeTable.createLearningTime(NEW_START_TIME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_시작_시간을_변경할_때_시작_시간이_중복되면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);
        timeTable.createLearningTime(NEW_START_TIME);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeStartTime(START_TIME, NEW_START_TIME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간이_다른_학습_시간과_겹치면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(60));

        // when then
        LocalDateTime newTime = START_TIME.plusMinutes(30);
        assertThatThrownBy(() -> timeTable.createLearningTime(newTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간을_변경할_수_있다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);

        // when
        LocalDateTime changedEndTime = timeTable.changeLearningTimeEndTime(START_TIME, END_TIME);

        // then
        assertThat(changedEndTime).isEqualTo(END_TIME);
    }

    @Test
    void 타임_테이블의_날짜보다_이전의_날짜를_가진_학습_시간을_추가하면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        LocalDateTime beforeStartTime = START_TIME.minusDays(3);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(beforeStartTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 타임_테이블의_날짜보다_2일_이후의_날짜를_가진_학습_시간을_추가하면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        LocalDateTime afterStartTime = START_TIME.plusDays(3);

        // when then
        assertThatThrownBy(() -> timeTable.createLearningTime(afterStartTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시작_시간에_해당하는_학습_시간이_없으면_에러가_발생한다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        // when then
        assertThatThrownBy(() -> timeTable.changeLearningTimeEndTime(START_TIME, END_TIME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간을_삭제할_수_있다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);

        // when
        timeTable.deleteLearningTime(START_TIME);

        // then
        assertThatThrownBy(() -> timeTable.deleteLearningTime(START_TIME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간은_0이_된다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isZero();
    }

    @Test
    void 학습_시간의_끝나는_시간이_정해지지_않으면_총_학습_시간에_포함되지_않는다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(50));
        timeTable.createLearningTime(NEW_START_TIME);

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(50);
    }

    @Test
    void 타임_테이블은_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        addLearningTime(timeTable, START_TIME, START_TIME.plusMinutes(40L));
        addLearningTime(timeTable, START_TIME.plusMinutes(45L), START_TIME.plusMinutes(80L));
        addLearningTime(timeTable, START_TIME.plusMinutes(90L), START_TIME.plusMinutes(120L));
        addLearningTime(timeTable, START_TIME.plusMinutes(140L), START_TIME.plusMinutes(200L));

        // when
        int totalLearningTime = timeTable.getTotalLearningTime();

        // then
        assertThat(totalLearningTime).isEqualTo(165);
    }

    @Test
    void 같은_학습_주제를_가진_학습_시간의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        LearningTime learningTime1 = addLearningTime(timeTable, START_TIME, START_TIME.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, START_TIME.plusMinutes(40), START_TIME.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, START_TIME.plusMinutes(100), START_TIME.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, START_TIME.plusMinutes(140), START_TIME.plusMinutes(230));

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
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        LearningTime learningTime1 = addLearningTime(timeTable, START_TIME, START_TIME.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, START_TIME.plusMinutes(40), START_TIME.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, START_TIME.plusMinutes(100), START_TIME.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, START_TIME.plusMinutes(140), START_TIME.plusMinutes(230));

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
        LocalDate startTimeDate = START_TIME.toLocalDate();
        TimeTable timeTable = new TimeTable(startTimeDate);

        LearningTime learningTime1 = addLearningTime(timeTable, START_TIME, START_TIME.plusMinutes(30));
        LearningTime learningTime2 = addLearningTime(timeTable, START_TIME.plusMinutes(40), START_TIME.plusMinutes(80));
        LearningTime learningTime3 = addLearningTime(timeTable, START_TIME.plusMinutes(100), START_TIME.plusMinutes(130));
        LearningTime learningTime4 = addLearningTime(timeTable, START_TIME.plusMinutes(140), START_TIME.plusMinutes(230));

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
