package hwicode.schedule.dailyschedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeAggregateService;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.NEW_START_TIME;
import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.START_TIME;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TaskSaveAndDeleteServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @Autowired
    LearningTimeAggregateService learningTimeAggregateService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제를_추가할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(timeTable.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);

        // then
        assertThat(taskRepository.existsById(taskId)).isTrue();
    }

    @Test
    void 과제를_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        Long subjectOfTaskId = saveSubjectOfTask(timeTable.getId());

        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(timeTable.getId(), subjectOfTaskId, TASK_NAME);

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        shouldDelete(TASK_NAME, taskDeleteRequest);
    }

    private void shouldDelete(String taskName, TaskDeleteRequest taskDeleteRequest) {
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(taskName, taskDeleteRequest))
                .isInstanceOf(NotValidExternalRequestException.class);
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

        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(timeTable.getId(), subjectOfTaskId, TASK_NAME);

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        shouldDelete(TASK_NAME, taskDeleteRequest);
        checkSubjectOfTaskIsDelete(learningTime.getId());
        checkSubjectOfTaskIsDelete(learningTime2.getId());
    }

    private Long saveSubjectOfTask(Long timeTableId) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(timeTableId).orElseThrow();
        Task savedTask = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
        return savedTask.getId();
    }

    private void checkSubjectOfTaskIsDelete(Long learningTimeId) {
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

}
