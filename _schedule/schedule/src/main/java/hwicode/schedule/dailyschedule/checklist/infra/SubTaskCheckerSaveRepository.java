package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
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
