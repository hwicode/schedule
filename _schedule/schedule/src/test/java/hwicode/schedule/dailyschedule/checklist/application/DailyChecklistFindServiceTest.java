package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class DailyChecklistFindServiceTest {

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    final Long DAILY_CHECKLIST_ID =  1L;

    @Test
    public void 존재하지_않는_체크리스트를_조회하면_에러가_발생한다() {
        // when then
        assertThatThrownBy(() -> DailyChecklistFindService.findDailyChecklistWithTasks(dailyChecklistRepository, DAILY_CHECKLIST_ID))
                .isInstanceOf(DailyChecklistNotFoundException.class);
    }
}
