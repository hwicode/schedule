package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskCheckerFindRepository {

    private final TaskCheckerRepository taskCheckerRepository;

    public Optional<TaskChecker> findTaskCheckerWithSubTaskCheckers(Long id) {
        return taskCheckerRepository.findTaskCheckerWithSubTaskCheckers(id);
    }
}
