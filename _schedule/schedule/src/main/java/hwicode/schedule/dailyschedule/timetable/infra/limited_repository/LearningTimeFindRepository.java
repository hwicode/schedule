package hwicode.schedule.dailyschedule.timetable.infra.limited_repository;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.infra.jpa_repository.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LearningTimeFindRepository {

    private final LearningTimeRepository learningTimeRepository;

    public Optional<LearningTime> findById(Long id) {
        return learningTimeRepository.findById(id);
    }
}
