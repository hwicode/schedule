package hwicode.schedule.cross_boundedcontext_test;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.review.application.ReviewTaskService;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.START_DATE;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ToDoListAndReviewTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @Autowired
    ReviewTaskService reviewTaskService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

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
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        Task savedTask = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
        reviewTask(savedTask, cycle);

        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(dailyToDoList.getId(), savedTask.getId(), TASK_NAME);

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest))
                .isInstanceOf(TaskCheckerNotFoundException.class);
        assertThat(reviewDateTaskRepository.findAll()).isEmpty();
    }

    private void reviewTask(Task task, List<Integer> cycle) {
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);
        reviewCycleRepository.save(reviewCycle);

        reviewTaskService.reviewTask(task.getId(), reviewCycle.getId(), START_DATE);
    }

    private static Stream<List<Integer>> provideReviewCycleDates() {
        return Stream.of(
                List.of(1, 2, 4, 7, 14, 60),
                List.of(4, 5, 6, 10, 20, 50),
                List.of(7, 8, 9, 12, 5, 2)
        );
    }

}
