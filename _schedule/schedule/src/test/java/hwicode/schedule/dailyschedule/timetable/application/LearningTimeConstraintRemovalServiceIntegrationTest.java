package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.*;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LearningTimeConstraintRemovalServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    LearningTimeConstraintRemovalService learningTimeConstraintRemovalService;

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
        learningTimeConstraintRemovalService.deleteSubjectOfTaskBelongingToLearningTime(subjectOfTask.getId());

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
        learningTimeConstraintRemovalService.deleteSubjectOfSubTaskBelongingToLearningTime(subjectOfSubTask.getId());

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
