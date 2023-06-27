package hwicode.schedule.timetable.infra.limited_repository;

import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.exception.application.TimeTableNotFoundException;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimeTableFindRepository {

    private final TimeTableRepository tableRepository;

    public TimeTable findTimeTableWithLearningTimes(Long id) {
        return tableRepository.findTimeTableWithLearningTimes(id)
                .orElseThrow(TimeTableNotFoundException::new);
    }
}
