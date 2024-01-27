package hwicode.schedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.TaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.TASK_CHECKER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class ChecklistAndToDoListTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @ParameterizedTest
    @MethodSource("provideTaskInformation")
    void 과제체커를_생성하면_과제의_긴급도와_중요도도_저장된다(Priority priority, Importance importance) {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveCommand command = new TaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL, priority, importance);

        // when
        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(command);

        // then
        Task task = taskRepository.findById(taskCheckerId).orElseThrow();
        assertThat(task.changePriority(priority)).isFalse();
        assertThat(task.changeImportance(importance)).isFalse();
    }

    private static Stream<Arguments> provideTaskInformation() {
        return Stream.of(
                arguments(Priority.FIRST, Importance.THIRD),
                arguments(Priority.FIRST, Importance.FIRST),
                arguments(Priority.SECOND, Importance.SECOND),
                arguments(Priority.THIRD, Importance.THIRD)
        );
    }

}
