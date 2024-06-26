package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DailyChecklistFindRepositoryTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyChecklistFindRepository dailyChecklistFindRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 존재하지_않는_체크리스트를_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(noneExistId))
                .isInstanceOf(DailyChecklistNotFoundException.class);
    }
}
