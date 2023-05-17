package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.infra.limited_repository.DailyToDoListFindRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyToDoListAggregateService {

    private final DailyToDoListFindRepository dailyToDoListFindRepository;

    @Transactional
    public Long changeDailyToDoListInformation(Long dailyToDoListId, DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest) {
        DailyToDoList dailyToDoList = dailyToDoListFindRepository.findById(dailyToDoListId);

        dailyToDoList.writeReview(dailyToDoListInformationChangeRequest.getReview());
        dailyToDoList.changeTodayEmoji(dailyToDoListInformationChangeRequest.getEmoji());
        return dailyToDoListId;
    }
}
