package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.DAILY_CHECKLIST_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class DailyChecklistFindServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    public void 존재하지_않는_체크리스트를_조회하면_에러가_발생한다() {
        // when then
        assertThatThrownBy(() -> DailyChecklistFindService.findDailyChecklistWithTasks(dailyChecklistRepository, DAILY_CHECKLIST_ID))
                .isInstanceOf(DailyChecklistNotFoundException.class);
    }
}
