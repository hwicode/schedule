package hwicode.schedule.dailyschedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.START_TIME;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ToDoListExternalIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void todolist_외부의_서비스를_호출할_때_외부에서_에러가_발생한다면_에러가_무엇인지_알_수_있다() {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();
        NotValidExternalRequestException notValidExternalRequestException = new NotValidExternalRequestException(dailyChecklistNotFoundException);

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        Long noneExistId = timeTable.getId() + 1;
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(noneExistId, TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when then
        try {
            taskSaveAndDeleteService.save(taskSaveRequest);
        } catch (NotValidExternalRequestException e) {
            assertThat(e.getMessage()).isEqualTo(notValidExternalRequestException.getMessage());
            assertThat(e.getExternalException().getMessage()).isEqualTo(notValidExternalRequestException.getExternalException().getMessage());
        }
    }

}
