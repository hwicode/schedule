package hwicode.schedule.dailyschedule.checklist.infra.limited_repository;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DailyChecklistFindRepository {

    private final DailyChecklistRepository dailyChecklistRepository;

    public DailyChecklist findDailyChecklistWithTaskCheckers(Long id) {
        return dailyChecklistRepository.findDailyChecklistWithTaskCheckers(id)
                .orElseThrow(DailyChecklistNotFoundException::new);
    }
}
