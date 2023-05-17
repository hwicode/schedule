package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.*;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetable.LearningTimeNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
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
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        learningTime.changeSubject(SUBJECT);
        timeTableRepository.save(timeTable);

        // when
        learningTimeAggregateService.deleteSubject(learningTime.getId());

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

    @Test
    void 학습_시간의_학습_주제를_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        // when
        learningTimeAggregateService.changeSubject(learningTime.getId(), SUBJECT);

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간의_Task_학습_주제를_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(SUBJECT));

        // when
        learningTimeAggregateService.changeSubjectOfTask(learningTime.getId(), subjectOfTask.getId());

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간의_SubTask_학습_주제를_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(SUBJECT));

        // when
        learningTimeAggregateService.changeSubjectOfSubTask(learningTime.getId(), subjectOfSubTask.getId());

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 존재하지_않는_학습_시간를_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> learningTimeAggregateService.deleteSubject(noneExistId))
                .isInstanceOf(LearningTimeNotFoundException.class);
    }

}
