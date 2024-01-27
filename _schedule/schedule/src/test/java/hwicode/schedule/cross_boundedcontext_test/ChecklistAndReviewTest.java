package hwicode.schedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.TaskDeleteCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.review.application.ReviewTaskService;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.START_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ChecklistAndReviewTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

    @Autowired
    ReviewTaskService reviewTaskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskCheckerRepository taskCheckerRepository;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @Autowired
    ReviewDateTaskRepository reviewDateTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @ParameterizedTest
    @MethodSource("provideReviewCycleDates")
    void 과제와_연관된_복습_과제가_있더라도_과제를_삭제할_수_있다(List<Integer> cycle) {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskChecker savedTask = taskCheckerRepository.save(new TaskChecker(dailyChecklist, "name", Difficulty.NORMAL, 1L));
        reviewTask(savedTask, cycle);

        TaskDeleteCommand command = new TaskDeleteCommand(userId, dailyChecklist.getId(), savedTask.getId(), "name");

        // when
        taskCheckerSubService.deleteTaskChecker(command);

        // then
        assertThatThrownBy(() -> taskCheckerSubService.deleteTaskChecker(command))
                .isInstanceOf(TaskCheckerNotFoundException.class);
        assertThat(reviewDateTaskRepository.findAll()).isEmpty();
    }

    private void reviewTask(TaskChecker taskChecker, List<Integer> cycle) {
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);
        reviewCycleRepository.save(reviewCycle);

        reviewTaskService.reviewTask(taskChecker.getId(), reviewCycle.getId(), START_DATE);
    }

    private static Stream<List<Integer>> provideReviewCycleDates() {
        return Stream.of(
                List.of(1, 2, 4, 7, 14, 60),
                List.of(4, 5, 6, 10, 20, 50),
                List.of(7, 8, 9, 12, 5, 2)
        );
    }

}
