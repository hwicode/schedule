package hwicode.schedule.timetable.infra.limited_repository;

import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.exception.application.SubjectOfSubTaskNotFoundException;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubjectOfSubTaskFindRepository {

    private final SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    public SubjectOfSubTask findById(Long id) {
        return subjectOfSubTaskRepository.findById(id)
                .orElseThrow(SubjectOfSubTaskNotFoundException::new);
    }
}
