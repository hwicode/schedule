package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimeTableFindRepository {

    private final TimeTableRepository tableRepository;

    public Optional<TimeTable> findTimeTableWithLearningTimes(Long id) {
        return tableRepository.findTimeTableWithLearningTimes(id);
    }
}
