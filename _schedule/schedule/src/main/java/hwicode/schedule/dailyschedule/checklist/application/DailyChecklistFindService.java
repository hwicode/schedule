package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyChecklistFindService {

    static DailyChecklist findDailyChecklistWithTaskCheckers(DailyChecklistRepository dailyChecklistRepository, Long dailyChecklistId) {
        return dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklistId)
                .orElseThrow(DailyChecklistNotFoundException::new);
    }
}
