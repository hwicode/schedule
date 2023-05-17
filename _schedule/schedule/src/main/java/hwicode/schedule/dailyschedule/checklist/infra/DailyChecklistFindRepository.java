package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DailyChecklistFindRepository {

    private final DailyChecklistRepository dailyChecklistRepository;

    public Optional<DailyChecklist> findDailyChecklistWithTaskCheckers(Long id) {
        return dailyChecklistRepository.findDailyChecklistWithTaskCheckers(id);
    }
}
