package hwicode.schedule.timetable.infra.limited_repository;

import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.exception.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubjectOfTaskFindRepository {

    private final SubjectOfTaskRepository subjectOfTaskRepository;

    public SubjectOfTask findById(Long id) {
        return subjectOfTaskRepository.findById(id)
                .orElseThrow(SubjectOfTaskNotFoundException::new);
    }
}
