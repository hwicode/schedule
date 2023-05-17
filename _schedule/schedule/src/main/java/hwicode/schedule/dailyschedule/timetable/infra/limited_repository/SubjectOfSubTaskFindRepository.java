package hwicode.schedule.dailyschedule.timetable.infra.limited_repository;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubjectOfSubTaskFindRepository {

    private final SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    public Optional<SubjectOfSubTask> findById(Long id) {
        return subjectOfSubTaskRepository.findById(id);
    }
}
