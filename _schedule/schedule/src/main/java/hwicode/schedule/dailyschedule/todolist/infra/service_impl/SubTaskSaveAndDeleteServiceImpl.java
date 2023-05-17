package hwicode.schedule.dailyschedule.todolist.infra.service_impl;

import hwicode.schedule.common.exception.BusinessException;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeConstraintRemovalService;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubTaskSaveAndDeleteServiceImpl implements SubTaskSaveAndDeleteService {

    private final SubTaskCheckerService subTaskCheckerService;
    private final LearningTimeConstraintRemovalService learningTimeConstraintRemovalService;

    @Override
    @Transactional
    public Long save(SubTaskSaveRequest subTaskSaveRequest) {
       try {
           return subTaskCheckerService.saveSubTaskChecker(
                   createSubTaskCheckerSaveRequest(subTaskSaveRequest)
           );
       } catch (BusinessException e) {
           throw new NotValidExternalRequestException(e);
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
    public Long delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        learningTimeConstraintRemovalService.deleteSubjectOfSubTaskBelongingToLearningTime(subTaskDeleteRequest.getSubTaskId());
        deleteSubTaskChecker(subTaskName, subTaskDeleteRequest);
        return subTaskDeleteRequest.getSubTaskId();
    }

    private void deleteSubTaskChecker(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        try {
            subTaskCheckerService.deleteSubTaskChecker(
                    subTaskName,
                    createSubTaskCheckerDeleteRequest(subTaskDeleteRequest)
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(e);
        }
    }

    private SubTaskCheckerDeleteRequest createSubTaskCheckerDeleteRequest(SubTaskDeleteRequest subTaskDeleteRequest) {
        return new SubTaskCheckerDeleteRequest(
                subTaskDeleteRequest.getDailyToDoListId(),
                subTaskDeleteRequest.getTaskName());
    }
}
