package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.DailyToDoListInformationCommand;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.infra.limited_repository.DailyToDoListFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyToDoListAggregateService {

    private final DailyToDoListFindRepository dailyToDoListFindRepository;

    @Transactional
    public Long changeDailyToDoListInformation(DailyToDoListInformationCommand command) {
        DailyToDoList dailyToDoList = dailyToDoListFindRepository.findById(command.getDailyToDoListId());
        dailyToDoList.checkOwnership(command.getUserId());

        dailyToDoList.writeReview(command.getReview());
        dailyToDoList.changeTodayEmoji(command.getEmoji());
        return dailyToDoList.getId();
    }
}
