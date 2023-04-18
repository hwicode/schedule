package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.common.exception.BusinessException;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
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
       try {
           return subTaskCheckerService.saveSubTaskChecker(
                   createSubTaskCheckerSaveRequest(subTaskSaveRequest)
           );
       } catch (BusinessException e) {
           throw new NotValidExternalRequestException();
       }
    }

    private SubTaskCheckerSaveRequest createSubTaskCheckerSaveRequest(SubTaskSaveRequest subTaskSaveRequest) {
        return new SubTaskCheckerSaveRequest(
                subTaskSaveRequest.getDailyToDoListId(),
                subTaskSaveRequest.getTaskName(),
                subTaskSaveRequest.getSubTaskName()
        );
    }

    @Override
    @Transactional
    public void delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        try {
            subTaskCheckerService.deleteSubTaskChecker(
                    subTaskName,
                    createSubTaskCheckerDeleteRequest(subTaskDeleteRequest)
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException();
        }
    }

    private SubTaskCheckerDeleteRequest createSubTaskCheckerDeleteRequest(SubTaskDeleteRequest subTaskDeleteRequest) {
        return new SubTaskCheckerDeleteRequest(
                subTaskDeleteRequest.getDailyToDoListId(),
                subTaskDeleteRequest.getTaskName());
    }
}
