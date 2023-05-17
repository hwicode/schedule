package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
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
