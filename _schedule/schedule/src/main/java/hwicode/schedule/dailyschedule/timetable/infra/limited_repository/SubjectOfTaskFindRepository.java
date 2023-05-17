package hwicode.schedule.dailyschedule.timetable.infra.limited_repository;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.exception.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
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
