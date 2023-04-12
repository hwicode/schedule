package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.DailyToDoListInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskNameChangeRequest;
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
                .orElseThrow(IllegalArgumentException::new);

        dailyToDoList.writeReview(dailyToDoListInformationChangeRequest.getReview());
        dailyToDoList.changeTodayEmoji(dailyToDoListInformationChangeRequest.getEmoji());
    }

    @Transactional
    public String changeTaskName(String taskName, TaskNameChangeRequest taskNameChangeRequest) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findToDoListWithTasks(taskNameChangeRequest.getDailyChecklistId())
                .orElseThrow(IllegalArgumentException::new);

        return dailyToDoList.changeTaskName(taskName, taskNameChangeRequest.getNewTaskName());
    }
}