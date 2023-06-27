package hwicode.schedule.timetable.infra.limited_repository;

import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LearningTimeSaveRepository {

    private final LearningTimeRepository learningTimeRepository;

    public LearningTime save(LearningTime learningTime) {
        return learningTimeRepository.save(learningTime);
    }
}
