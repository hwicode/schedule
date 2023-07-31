package hwicode.schedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.TimeTableDataHelper;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.SubjectOfSubTaskConstraintRemover;
import hwicode.schedule.timetable.infra.SubjectOfTaskConstraintRemover;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LearningTimeConstraintRemoverIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubjectOfTaskConstraintRemover subjectOfTaskConstraintRemover;

    @Autowired
    SubjectOfSubTaskConstraintRemover subjectOfSubTaskConstraintRemover;

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
        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(TimeTableDataHelper.SUBJECT));

        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        learningTime.changeSubjectOfTask(subjectOfTask);
        LearningTime learningTime2 = timeTable.createLearningTime(TimeTableDataHelper.NEW_START_TIME);
        learningTime2.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        // when
        subjectOfTaskConstraintRemover.delete(subjectOfTask.getId());

        // then
        checkSubjectIsDelete(learningTime.getId());
        checkSubjectIsDelete(learningTime2.getId());
    }

    @Test
    void 학습_시간들에_연관된_SubTask_학습_주제를_삭제할_수_있다() {
        // given
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));

        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);
        LearningTime learningTime2 = timeTable.createLearningTime(TimeTableDataHelper.NEW_START_TIME);
        learningTime2.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        // when
        subjectOfSubTaskConstraintRemover.delete(subjectOfSubTask.getId());

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
