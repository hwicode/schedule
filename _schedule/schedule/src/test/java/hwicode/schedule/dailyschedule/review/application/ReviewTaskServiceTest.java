package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ReviewTaskServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewTaskService reviewTaskService;

    @Autowired
    ReviewTaskRepository reviewTaskRepository;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @Autowired
    ReviewDateTaskRepository reviewDateTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 복습_주기에_맞춰_과제를_복습할_수_있다() {
        // given
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        // when
        reviewTaskService.reviewTask(reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // then
        assertThat(reviewDateTaskRepository.findAll()).hasSize(cycle.size());
    }

}
