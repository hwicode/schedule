package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LearningTimeServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    LearningTimeService learningTimeService;

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
        learningTimeService.deleteSubject(learningTime.getId());

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
        learningTimeService.changeSubject(learningTime.getId(), SUBJECT);

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
        learningTimeService.changeSubjectOfTask(learningTime.getId(), subjectOfTask.getId());

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
        learningTimeService.changeSubjectOfSubTask(learningTime.getId(), subjectOfSubTask.getId());

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간들에_연관된_Task_학습_주제를_삭제할_수_있다() {
        // given
        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(SUBJECT));

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        learningTime.changeSubjectOfTask(subjectOfTask);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        learningTime2.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        // when
        learningTimeService.deleteSubjectOfTaskBelongingToLearningTime(subjectOfTask.getId());

        // then
        checkSubjectIsDelete(learningTime.getId());
        checkSubjectIsDelete(learningTime2.getId());
    }

    @Test
    void 학습_시간들에_연관된_SubTask_학습_주제를_삭제할_수_있다() {
        // given
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(SUBJECT));

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        learningTime2.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        // when
        learningTimeService.deleteSubjectOfSubTaskBelongingToLearningTime(subjectOfSubTask.getId());

        // then
        checkSubjectIsDelete(learningTime.getId());
        checkSubjectIsDelete(learningTime2.getId());
    }

    private void checkSubjectIsDelete(Long learningTimeId) {
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }
}
