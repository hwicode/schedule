package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
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
    LearningTimeService learningTimeService;

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
    void ToDo_리스트에_과제를_추가할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(dailyToDoList.getId(), TASK_NAME);

        // when
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);

        // then
        assertThat(taskRepository.existsById(taskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);
        Task savedTask = taskRepository.save(new Task(dailyToDoList, TASK_NAME));

        TaskDeleteRequest taskDeleteRequest = createTaskDeleteRequest(dailyToDoList.getId(), savedTask.getId());

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest))
                .isInstanceOf(NotValidExternalRequestException.class);
    }

    @Test
    void 과제와_연관된_학습_시간이_있더라도_ToDo_리스트에_과제를_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        LearningTime learningTime2 = timeTable.createLearningTime(NEW_START_TIME);
        timeTableRepository.save(timeTable);

        Long subjectOfTaskId = saveSubjectOfTask(timeTable.getId());

        learningTimeService.changeSubjectOfTask(learningTime.getId(), subjectOfTaskId);
        learningTimeService.changeSubjectOfTask(learningTime2.getId(), subjectOfTaskId);

        TaskDeleteRequest taskDeleteRequest = createTaskDeleteRequest(timeTable.getId(), subjectOfTaskId);

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest))
                .isInstanceOf(NotValidExternalRequestException.class);

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();

        LearningTime savedLearningTime2 = learningTimeRepository.findById(learningTime2.getId()).orElseThrow();
        boolean isDelete2 = savedLearningTime2.deleteSubject();
        assertThat(isDelete2).isFalse();
    }

    private Long saveSubjectOfTask(Long timeTableId) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(timeTableId).orElseThrow();
        Task savedTask = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
        return savedTask.getId();
    }

}
