package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubjectOfTaskFindRepository {

    private final SubjectOfTaskRepository subjectOfTaskRepository;

    public Optional<SubjectOfTask> findById(Long id) {
        return subjectOfTaskRepository.findById(id);
    }
}
