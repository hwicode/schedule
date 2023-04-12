package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyToDoListService {

    private final DailyToDoListRepository dailyToDoListRepository;

    @Transactional
    public void changeDailyToDoListInformation(Long dailyToDoListId, String review, Emoji emoji) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findById(dailyToDoListId)
                .orElseThrow(IllegalArgumentException::new);

        dailyToDoList.writeReview(review);
        dailyToDoList.changeTodayEmoji(emoji);
    }

    @Transactional
    public String changeTaskName(Long dailyToDoListId, String taskName, String newTaskName) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findToDoListWithTasks(dailyToDoListId)
                .orElseThrow(IllegalArgumentException::new);

        return dailyToDoList.changeTaskName(taskName, newTaskName);
    }
}
