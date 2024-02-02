package hwicode.schedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.common.login.validator.OwnerForbiddenException;
import hwicode.schedule.timetable.TimeTableDataHelper;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeDeleteSubjectCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfSubTaskCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfTaskCommand;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.exception.LearningTimeNotFoundException;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LearningTimeAggregateServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    LearningTimeAggregateService learningTimeAggregateService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @Autowired
    SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_시간의_학습_주제를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        learningTime.changeSubject(TimeTableDataHelper.SUBJECT);
        timeTableRepository.save(timeTable);

        LearningTimeDeleteSubjectCommand command = new LearningTimeDeleteSubjectCommand(userId, learningTime.getId());

        // when
        learningTimeAggregateService.deleteSubject(command);

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

    @Test
    void 학습_시간의_학습_주제를_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        learningTime.changeSubject(TimeTableDataHelper.SUBJECT);
        timeTableRepository.save(timeTable);

        LearningTimeDeleteSubjectCommand command = new LearningTimeDeleteSubjectCommand(2L, learningTime.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.deleteSubject(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_학습_주제를_수정할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        LearningTimeModifySubjectCommand command = new LearningTimeModifySubjectCommand(userId, learningTime.getId(), TimeTableDataHelper.SUBJECT);

        // when
        learningTimeAggregateService.changeSubject(command);

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간의_학습_주제를_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        LearningTimeModifySubjectCommand command = new LearningTimeModifySubjectCommand(2L, learningTime.getId(), TimeTableDataHelper.SUBJECT);

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubject(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_수정할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(TimeTableDataHelper.SUBJECT, userId));

        LearningTimeModifySubjectOfTaskCommand command = new LearningTimeModifySubjectOfTaskCommand(userId, learningTime.getId(), subjectOfTask.getId());

        // when
        learningTimeAggregateService.changeSubjectOfTask(command);

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간의_Task_학습_주제를_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(TimeTableDataHelper.SUBJECT, userId));

        LearningTimeModifySubjectOfTaskCommand command = new LearningTimeModifySubjectOfTaskCommand(2L, learningTime.getId(), subjectOfTask.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubjectOfTask(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_수정할_때_학습_시간의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), 2L);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(TimeTableDataHelper.SUBJECT, userId));

        LearningTimeModifySubjectOfTaskCommand command = new LearningTimeModifySubjectOfTaskCommand(userId, learningTime.getId(), subjectOfTask.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubjectOfTask(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_Task_학습_주제를_수정할_때_Task의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(TimeTableDataHelper.SUBJECT, 2L));

        LearningTimeModifySubjectOfTaskCommand command = new LearningTimeModifySubjectOfTaskCommand(userId, learningTime.getId(), subjectOfTask.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubjectOfTask(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_수정할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT, userId));

        LearningTimeModifySubjectOfSubTaskCommand command = new LearningTimeModifySubjectOfSubTaskCommand(userId, learningTime.getId(), subjectOfSubTask.getId());

        // when
        learningTimeAggregateService.changeSubjectOfSubTask(command);

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT, userId));

        LearningTimeModifySubjectOfSubTaskCommand command = new LearningTimeModifySubjectOfSubTaskCommand(2L, learningTime.getId(), subjectOfSubTask.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubjectOfSubTask(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_수정할_때_학습_시간의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), 2L);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT, userId));

        LearningTimeModifySubjectOfSubTaskCommand command = new LearningTimeModifySubjectOfSubTaskCommand(userId, learningTime.getId(), subjectOfSubTask.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubjectOfSubTask(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_수정할_때_SubTask의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT, 2L));

        LearningTimeModifySubjectOfSubTaskCommand command = new LearningTimeModifySubjectOfSubTaskCommand(userId, learningTime.getId(), subjectOfSubTask.getId());

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.changeSubjectOfSubTask(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 존재하지_않는_학습_시간를_조회하면_에러가_발생한다() {
        //given
        Long userId = 1L;
        Long noneExistId = 1L;

        LearningTimeDeleteSubjectCommand command = new LearningTimeDeleteSubjectCommand(userId, noneExistId);

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.deleteSubject(command))
                .isInstanceOf(LearningTimeNotFoundException.class);
    }

}
