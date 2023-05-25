package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.limited_repository.TaskFindRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskAggregateService {

    private final TaskFindRepository taskFindRepository;

    @Transactional
    public Long changeTaskInformation(Long taskId, TaskInformationModifyRequest taskInformationModifyRequest) {
        Task task = taskFindRepository.findById(taskId);

        task.changePriority(taskInformationModifyRequest.getPriority());
        task.changeImportance(taskInformationModifyRequest.getImportance());
        return taskId;
    }
}