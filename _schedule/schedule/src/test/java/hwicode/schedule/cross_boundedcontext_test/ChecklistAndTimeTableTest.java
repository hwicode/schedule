package hwicode.schedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.timetable.application.LearningTimeAggregateService;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.SUB_TASK_NAME;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
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
    SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @Autowired
    LearningTimeAggregateService learningTimeAggregateService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskCheckerRepository taskCheckerRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

//    @Test
//    void 과제와_연관된_학습_시간이_있더라도_과제를_삭제할_수_있다() {
//        // given
//        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
//        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
//        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
//        timeTableRepository.save(timeTable);
//
//        Long subjectOfTaskId = saveSubjectOfTask(timeTable.getId());
//
//        learningTimeAggregateService.changeSubjectOfTask(learningTime.getId(), subjectOfTaskId);
//        learningTimeAggregateService.changeSubjectOfTask(learningTime2.getId(), subjectOfTaskId);
//
//        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(timeTable.getId(), subjectOfTaskId, TASK_NAME);
//
//        // when
//        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);
//
//        // then
//        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest))
//                .isInstanceOf(TaskCheckerNotFoundException.class);
//        checkSubjectOfTaskIsDelete(learningTime.getId());
//        checkSubjectOfTaskIsDelete(learningTime2.getId());
//    }
//
//    private Long saveSubjectOfTask(Long timeTableId) {
//        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(timeTableId).orElseThrow();
//        Task savedTask = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
//        return savedTask.getId();
//    }
//
//    private void checkSubjectOfTaskIsDelete(Long learningTimeId) {
//        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
//        boolean isDelete = savedLearningTime.deleteSubject();
//        assertThat(isDelete).isFalse();
//    }
//
//    @Test
//    void 서브_과제와_연관된_학습_시간이_있더라도_서브_과제를_삭제할_수_있다() {
//        // given
//        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
//        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
//        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
//        timeTableRepository.save(timeTable);
//
//        Long subjectOfSubTaskId = saveSubjectOfSubTask(timeTable.getId());
//
//        learningTimeAggregateService.changeSubjectOfSubTask(learningTime.getId(), subjectOfSubTaskId);
//        learningTimeAggregateService.changeSubjectOfSubTask(learningTime2.getId(), subjectOfSubTaskId);
//
//        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(timeTable.getId(), TASK_NAME, subjectOfSubTaskId, SUB_TASK_NAME);
//
//        // when
//        subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest);
//
//        // then
//        assertThatThrownBy(() -> subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest))
//                .isInstanceOf(SubTaskCheckerNotFoundException.class);
//        checkSubjectOfSubTaskIsDelete(learningTime.getId());
//        checkSubjectOfSubTaskIsDelete(learningTime2.getId());
//    }
//
//    private Long saveSubjectOfSubTask(Long timeTableId) {
//        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(timeTableId).orElseThrow();
//        Task task = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
//        SubTask subTask = subTaskRepository.save(new SubTask(task, SUB_TASK_NAME));
//        return subTask.getId();
//    }
//
//    private void checkSubjectOfSubTaskIsDelete(Long learningTimeId) {
//        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
//        boolean isDelete = savedLearningTime.deleteSubject();
//        assertThat(isDelete).isFalse();
//    }

}
