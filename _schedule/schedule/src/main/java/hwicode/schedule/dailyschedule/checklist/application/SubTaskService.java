package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskSaveOnlyRepository;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubTaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final SubTaskSaveOnlyRepository subTaskSaveOnlyRepository;

    public SubTaskService(DailyChecklistRepository dailyChecklistRepository, SubTaskSaveOnlyRepository subTaskSaveOnlyRepository) {
        this.dailyChecklistRepository = dailyChecklistRepository;
        this.subTaskSaveOnlyRepository = subTaskSaveOnlyRepository;
    }

    @Transactional
    public void saveSubTask(Long dailyChecklistId, String taskName, SubTask subTask) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.addSubTask(taskName, subTask);
        subTaskSaveOnlyRepository.save(subTask);
    }
}
