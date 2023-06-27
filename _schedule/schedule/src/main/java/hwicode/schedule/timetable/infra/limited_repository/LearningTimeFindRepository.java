package hwicode.schedule.timetable.infra.limited_repository;

import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.exception.LearningTimeNotFoundException;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LearningTimeFindRepository {

    private final LearningTimeRepository learningTimeRepository;

    public LearningTime findById(Long id) {
        return learningTimeRepository.findById(id)
                .orElseThrow(LearningTimeNotFoundException::new);
    }
}
