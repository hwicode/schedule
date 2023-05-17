package hwicode.schedule.dailyschedule.checklist.infra.limited_repository;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.SubTaskCheckerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerSaveRepository {

    private final SubTaskCheckerRepository subTaskCheckerRepository;

    public SubTaskChecker save(SubTaskChecker subTaskChecker) {
        return subTaskCheckerRepository.save(subTaskChecker);
    }
}
