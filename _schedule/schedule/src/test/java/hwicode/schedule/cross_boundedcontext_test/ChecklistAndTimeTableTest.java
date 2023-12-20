package hwicode.schedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.timetable.application.LearningTimeAggregateService;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.SUB_TASK_CHECKER_NAME;
import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.TASK_CHECKER_NAME;
import static hwicode.schedule.timetable.TimeTableDataHelper.NEW_START_TIME;
import static hwicode.schedule.timetable.TimeTableDataHelper.START_TIME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ChecklistAndTimeTableTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

    @Autowired
    SubTaskCheckerSubService subTaskCheckerSubService;

    @Autowired
    LearningTimeAggregateService learningTimeAggregateService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskCheckerRepository taskCheckerRepository;

    @Autowired
    SubTaskCheckerRepository subTaskCheckerRepository;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제와_연관된_학습_시간이_있더라도_과제를_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        timeTableRepository.save(timeTable);

        Long subjectOfTaskId = saveSubjectOfTask(timeTable.getId());

        learningTimeAggregateService.changeSubjectOfTask(learningTime.getId(), subjectOfTaskId);
        learningTimeAggregateService.changeSubjectOfTask(learningTime2.getId(), subjectOfTaskId);

        // when
        taskCheckerSubService.deleteTaskChecker(timeTable.getId(), subjectOfTaskId, TASK_CHECKER_NAME);

        // then
        assertThatThrownBy(() -> taskCheckerSubService.deleteTaskChecker(timeTable.getId(), subjectOfTaskId, TASK_CHECKER_NAME))
                .isInstanceOf(TaskCheckerNotFoundException.class);
        checkSubjectOfTaskIsDelete(learningTime.getId());
        checkSubjectOfTaskIsDelete(learningTime2.getId());
    }

    private Long saveSubjectOfTask(Long timeTableId) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findById(timeTableId).orElseThrow();
        TaskChecker taskChecker = taskCheckerRepository.save(new TaskChecker(dailyChecklist, TASK_CHECKER_NAME, Difficulty.NORMAL));
        return taskChecker.getId();
    }

    private void checkSubjectOfTaskIsDelete(Long learningTimeId) {
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

    @Test
    void 서브_과제와_연관된_학습_시간이_있더라도_서브_과제를_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        timeTableRepository.save(timeTable);

        Long subjectOfSubTaskId = saveSubjectOfSubTask(timeTable.getId());

        learningTimeAggregateService.changeSubjectOfSubTask(learningTime.getId(), subjectOfSubTaskId);
        learningTimeAggregateService.changeSubjectOfSubTask(learningTime2.getId(), subjectOfSubTaskId);

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(timeTable.getId(), TASK_CHECKER_NAME, subjectOfSubTaskId, SUB_TASK_CHECKER_NAME);

        // when
        subTaskCheckerSubService.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME, subTaskDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskCheckerSubService.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME, subTaskDeleteRequest))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
        checkSubjectOfSubTaskIsDelete(learningTime.getId());
        checkSubjectOfSubTaskIsDelete(learningTime2.getId());
    }

    private Long saveSubjectOfSubTask(Long timeTableId) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findById(timeTableId).orElseThrow();
        TaskChecker taskChecker = taskCheckerRepository.save(new TaskChecker(dailyChecklist, TASK_CHECKER_NAME, Difficulty.NORMAL));
        SubTaskChecker subTaskChecker = subTaskCheckerRepository.save(new SubTaskChecker(taskChecker, SUB_TASK_CHECKER_NAME));
        return subTaskChecker.getId();
    }

    private void checkSubjectOfSubTaskIsDelete(Long learningTimeId) {
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

}
