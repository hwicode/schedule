package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyChecklistFindService {

    static DailyChecklist findDailyChecklistWithTaskCheckers(DailyChecklistFindRepository dailyChecklistFindRepository, Long dailyChecklistId) {
        return dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(dailyChecklistId)
                .orElseThrow(DailyChecklistNotFoundException::new);
    }
}
