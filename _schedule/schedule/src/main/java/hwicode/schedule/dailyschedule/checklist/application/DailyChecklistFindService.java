package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyChecklistFindService {

    static DailyChecklist findDailyChecklistWithTasks(DailyChecklistRepository dailyChecklistRepository, Long dailyChecklistId) {
        return dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow(DailyChecklistNotFoundException::new);
    }
}
