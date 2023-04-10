package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.domain.SubTaskSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubTaskSaveServiceImpl implements SubTaskSaveService {

    private final SubTaskCheckerService subTaskCheckerService;
    private final SubTaskRepository subTaskRepository;

    @Override
    public SubTask save(SubTask subTask) {
        SubTask savedSubTask = subTaskRepository.save(subTask);

        subTaskCheckerService.saveSubTask(
                createSubTaskCheckerSaveRequest(savedSubTask)
        );

        return savedSubTask;
    }

    private SubTaskCheckerSaveRequest createSubTaskCheckerSaveRequest(SubTask subTask) {
        return new SubTaskCheckerSaveRequest(
                subTask.getDailyToDoListId(),
                subTask.getTaskName(),
                subTask.getName()
        );
    }
}
