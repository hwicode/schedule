package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskCheckerSaveRepository {

    private final TaskCheckerRepository taskCheckerRepository;

    public TaskChecker save(TaskChecker taskChecker) {
        return taskCheckerRepository.save(taskChecker);
    }
}
