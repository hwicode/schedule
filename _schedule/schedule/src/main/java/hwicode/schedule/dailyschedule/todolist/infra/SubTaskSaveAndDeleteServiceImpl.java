package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubTaskSaveAndDeleteServiceImpl implements SubTaskSaveAndDeleteService {

    private final SubTaskCheckerService subTaskCheckerService;

    @Override
    @Transactional
    public Long save(SubTaskSaveRequest subTaskSaveRequest) {
       return subTaskCheckerService.saveSubTaskChecker(
                new SubTaskCheckerSaveRequest(
                        subTaskSaveRequest.getDailyChecklistId(),
                        subTaskSaveRequest.getTaskName(),
                        subTaskSaveRequest.getSubTaskName()
                )
        );
    }

    @Override
    @Transactional
    public void delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        subTaskCheckerService.deleteSubTaskChecker(
                subTaskName,
                new SubTaskCheckerDeleteRequest(
                        subTaskDeleteRequest.getDailyChecklistId(),
                        subTaskDeleteRequest.getTaskName())
        );
    }
}
