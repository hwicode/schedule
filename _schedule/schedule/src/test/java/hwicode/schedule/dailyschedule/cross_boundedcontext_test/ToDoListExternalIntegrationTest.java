package hwicode.schedule.dailyschedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ToDoListExternalIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void todolist_외부의_서비스를_호출할_때_외부에서_에러가_발생한다면_에러가_무엇인지_알_수_있다() {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();
        NotValidExternalRequestException notValidExternalRequestException = new NotValidExternalRequestException(
                List.of(dailyChecklistNotFoundException.getMessage())
        );

        Long noneExistId = 1L;
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(noneExistId, TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when then
        try {
            taskSaveAndDeleteService.save(taskSaveRequest);
        } catch (NotValidExternalRequestException e) {
            assertThat(e.getMessage()).isEqualTo(notValidExternalRequestException.getMessage());
            assertThat(e.getExternalErrorMessages().get(0))
                    .isEqualTo(notValidExternalRequestException.getExternalErrorMessages().get(0));
        }
    }

}
