package hwicode.schedule.dailyschedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeAggregateService;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SubTaskSaveAndDeleteServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @Autowired
    LearningTimeAggregateService learningTimeAggregateService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

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

    private Long saveSubjectOfSubTask(Long timeTableId) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(timeTableId).orElseThrow();
        Task task = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
        SubTask subTask = subTaskRepository.save(new SubTask(task, SUB_TASK_NAME));
        return subTask.getId();
    }

    @Test
    void ToDo_리스트에_서브_과제를_추가할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        saveSubjectOfSubTask(timeTable.getId());

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(timeTable.getId(), TASK_NAME, NEW_SUBJECT);

        // when
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);

        // then
        assertThat(subTaskRepository.existsById(subTaskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_서브_과제를_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        Long subjectOfSubTaskId = saveSubjectOfSubTask(timeTable.getId());

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(timeTable.getId(), TASK_NAME, subjectOfSubTaskId, SUB_TASK_NAME);

        // when
        subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        shouldDelete(SUB_TASK_NAME, subTaskDeleteRequest);
    }

    private void shouldDelete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        assertThatThrownBy(() -> subTaskSaveAndDeleteService.delete(subTaskName, subTaskDeleteRequest))
                .isInstanceOf(NotValidExternalRequestException.class);
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

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(timeTable.getId(), TASK_NAME, subjectOfSubTaskId, SUB_TASK_NAME);

        // when
        subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        shouldDelete(SUB_TASK_NAME, subTaskDeleteRequest);
        checkSubjectOfSubTaskIsDelete(learningTime.getId());
        checkSubjectOfSubTaskIsDelete(learningTime2.getId());
    }

    private void checkSubjectOfSubTaskIsDelete(Long learningTimeId) {
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTimeId).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

}