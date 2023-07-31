package hwicode.schedule.dailyschedule.todolist.infra.service_impl;

import hwicode.schedule.common.exception.BusinessException;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubTaskSaveAndDeleteServiceImpl implements SubTaskSaveAndDeleteService {

    private final SubTaskCheckerSubService subTaskCheckerSubService;
    private final List<SubTaskConstraintRemover> subTaskConstraintRemovers;

    @Override
    @Transactional
    public Long save(SubTaskSaveRequest subTaskSaveRequest) {
       try {
           return subTaskCheckerSubService.saveSubTaskChecker(
                   createSubTaskCheckerSaveRequest(subTaskSaveRequest)
           );
       } catch (BusinessException e) {
           throw new NotValidExternalRequestException(
                   List.of(e.getMessage())
           );
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
        Long subTaskId = subTaskDeleteRequest.getSubTaskId();
        deleteForeignKeyConstraint(subTaskId);
        deleteSubTaskChecker(subTaskName, subTaskDeleteRequest);
        return subTaskId;
    }

    // 하나의 테이블에 여러 개의 엔티티가 매핑되어 있다. 다른 바운디드 컨텍스트에서 SubTask 테이블과의 매핑을 제거하는 메서드
    private void deleteForeignKeyConstraint(Long subTaskId) {
        subTaskConstraintRemovers.forEach(
                subTaskConstraintRemover -> subTaskConstraintRemover.delete(subTaskId)
        );
    }

    private void deleteSubTaskChecker(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        try {
            subTaskCheckerSubService.deleteSubTaskChecker(
                    subTaskName,
                    createSubTaskCheckerDeleteRequest(subTaskDeleteRequest)
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(
                    List.of(e.getMessage())
            );
        }
    }

    private SubTaskCheckerDeleteRequest createSubTaskCheckerDeleteRequest(SubTaskDeleteRequest subTaskDeleteRequest) {
        return new SubTaskCheckerDeleteRequest(
                subTaskDeleteRequest.getDailyToDoListId(),
                subTaskDeleteRequest.getTaskName());
    }
}
