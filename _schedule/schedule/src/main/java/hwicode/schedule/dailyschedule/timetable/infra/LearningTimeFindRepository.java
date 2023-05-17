package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
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
