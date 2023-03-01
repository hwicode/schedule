package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;

public class DailyChecklistFindService {

    static DailyChecklist findDailyChecklistWithTasks(DailyChecklistRepository dailyChecklistRepository, Long dailyChecklistId) {
        return dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();
    }
}
