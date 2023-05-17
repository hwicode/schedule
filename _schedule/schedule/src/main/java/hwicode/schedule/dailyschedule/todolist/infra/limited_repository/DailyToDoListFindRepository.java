package hwicode.schedule.dailyschedule.todolist.infra.limited_repository;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
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
