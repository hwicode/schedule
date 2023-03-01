package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubTaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final SubTaskRepository subTaskRepository;

    public SubTaskService(DailyChecklistRepository dailyChecklistRepository, SubTaskRepository subTaskRepository) {
        this.dailyChecklistRepository = dailyChecklistRepository;
        this.subTaskRepository = subTaskRepository;
    }

    @Transactional
    public void saveSubTask(Long dailyChecklistId, String taskName, SubTask subTask) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.addSubTask(taskName, subTask);
        subTaskRepository.save(subTask);
    }
}
