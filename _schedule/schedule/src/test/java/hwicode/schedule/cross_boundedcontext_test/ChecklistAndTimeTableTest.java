package hwicode.schedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskDeleteCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.TaskDeleteCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.timetable.application.LearningTimeAggregateService;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfSubTaskCommand;
import hwicode.schedule.timetable.application.dto.learning_time.LearningTimeModifySubjectOfTaskCommand;
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
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        timeTableRepository.save(timeTable);

        Long subjectOfTaskId = saveSubjectOfTask(timeTable.getId(), userId);

        LearningTimeModifySubjectOfTaskCommand command = new LearningTimeModifySubjectOfTaskCommand(userId, learningTime.getId(), subjectOfTaskId);
        LearningTimeModifySubjectOfTaskCommand command2 = new LearningTimeModifySubjectOfTaskCommand(userId, learningTime.getId(), subjectOfTaskId);

        learningTimeAggregateService.changeSubjectOfTask(command);
        learningTimeAggregateService.changeSubjectOfTask(command2);

        TaskDeleteCommand deleteCommand = new TaskDeleteCommand(userId, timeTable.getId(), subjectOfTaskId, TASK_CHECKER_NAME);

        // when
        taskCheckerSubService.deleteTaskChecker(deleteCommand);

        // then
        assertThatThrownBy(() -> taskCheckerSubService.deleteTaskChecker(deleteCommand))
                .isInstanceOf(TaskCheckerNotFoundException.class);
        checkSubjectOfTaskIsDelete(learningTime.getId());
        checkSubjectOfTaskIsDelete(learningTime2.getId());
    }

    private Long saveSubjectOfTask(Long timeTableId, Long userId) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findById(timeTableId).orElseThrow();
        TaskChecker taskChecker = taskCheckerRepository.save(new TaskChecker(dailyChecklist, TASK_CHECKER_NAME, Difficulty.NORMAL, userId));
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
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        timeTableRepository.save(timeTable);

        Long subjectOfSubTaskId = saveSubjectOfSubTask(timeTable.getId(), userId);

        LearningTimeModifySubjectOfSubTaskCommand command = new LearningTimeModifySubjectOfSubTaskCommand(userId, learningTime.getId(), subjectOfSubTaskId);
        LearningTimeModifySubjectOfSubTaskCommand command2 = new LearningTimeModifySubjectOfSubTaskCommand(userId, learningTime2.getId(), subjectOfSubTaskId);

        learningTimeAggregateService.changeSubjectOfSubTask(command);
        learningTimeAggregateService.changeSubjectOfSubTask(command2);

        SubTaskDeleteCommand deleteCommand = new SubTaskDeleteCommand(userId, timeTable.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, subjectOfSubTaskId);

        // when
        subTaskCheckerSubService.deleteSubTaskChecker(deleteCommand);

        // then
        assertThatThrownBy(() -> subTaskCheckerSubService.deleteSubTaskChecker(deleteCommand))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
        checkSubjectOfSubTaskIsDelete(learningTime.getId());
        checkSubjectOfSubTaskIsDelete(learningTime2.getId());
    }

    private Long saveSubjectOfSubTask(Long timeTableId, Long userId) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findById(timeTableId).orElseThrow();
        TaskChecker taskChecker = taskCheckerRepository.save(new TaskChecker(dailyChecklist, TASK_CHECKER_NAME, Difficulty.NORMAL, userId));
        SubTaskChecker subTaskChecker = subTaskCheckerRepository.save(new SubTaskChecker(taskChecker, SUB_TASK_CHECKER_NAME, userId));
        return subTaskChecker.getId();
    }

    private void checkSubjectOfSubTaskIsDelete(Long learningTimeId) {
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

}
