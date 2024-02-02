package hwicode.schedule.timetable.application.query;

import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TimeTableQueryService {

    private final TimeTableRepository timeTableRepository;

    @Transactional(readOnly = true)
    public List<LearningTimeQueryResponse> getLearningTimeQueryResponses(Long userId, LocalDate date) {
        return timeTableRepository.findLearningTimeQueryResponsesBy(userId, date);
    }

}
