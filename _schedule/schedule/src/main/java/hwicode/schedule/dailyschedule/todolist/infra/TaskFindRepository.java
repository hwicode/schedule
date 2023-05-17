package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.todolist.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskFindRepository {

    private final TaskRepository taskRepository;

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }
}
