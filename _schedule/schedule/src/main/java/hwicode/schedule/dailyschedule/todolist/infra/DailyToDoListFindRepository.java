package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DailyToDoListFindRepository {

    private final DailyToDoListRepository dailyToDoListRepository;

    public Optional<DailyToDoList> findById(Long id) {
        return dailyToDoListRepository.findById(id);
    }
}
