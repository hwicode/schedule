package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.DailyToDoListInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.exception.application.DailyToDoListNotExistException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.name_modify.TaskNameModifyRequest;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyToDoListService {

    private final DailyToDoListRepository dailyToDoListRepository;

    @Transactional
    public void changeDailyToDoListInformation(Long dailyToDoListId, DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(dailyToDoListId)
                .orElseThrow(DailyToDoListNotExistException::new);

        dailyToDoList.writeReview(dailyToDoListInformationChangeRequest.getReview());
        dailyToDoList.changeTodayEmoji(dailyToDoListInformationChangeRequest.getEmoji());
    }

    @Transactional
    public String changeTaskName(String taskName, TaskNameModifyRequest taskNameModifyRequest) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findToDoListWithTasks(taskNameModifyRequest.getDailyToDoListId())
                .orElseThrow(DailyToDoListNotExistException::new);

        return dailyToDoList.changeTaskName(taskName, taskNameModifyRequest.getNewTaskName());
    }
}
