package hwicode.schedule.calendar.application.query;

import hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse;
import hwicode.schedule.calendar.application.query.dto.GoalQueryResponse;
import hwicode.schedule.calendar.application.query.dto.SubGoalQueryResponse;
import hwicode.schedule.calendar.exception.application.CalendarNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class CalendarQueryService {

    private final CalendarRepository calendarRepository;
    private final CalendarGoalRepository calendarGoalRepository;
    private final GoalRepository goalRepository;

    @Transactional(readOnly = true)
    public CalendarQueryResponse getCalendarQueryResponse(Long userId, YearMonth yearMonth) {
        CalendarQueryResponse calendarQueryResponse = calendarRepository.findCalendarQueryResponseBy(userId, yearMonth)
                .orElseThrow(CalendarNotFoundException::new);

        List<GoalQueryResponse> goalQueryResponses = calendarGoalRepository.findGoalQueryResponseBy(calendarQueryResponse.getId());

        List<Long> goalIds = getGoalIds(goalQueryResponses);
        Map<Long, List<SubGoalQueryResponse>> subGoalQueryResponseMap = goalRepository.findSubGoalQueryResponsesBy(goalIds)
                .stream()
                .collect(groupingBy(SubGoalQueryResponse::getGoalId));

        goalQueryResponses.forEach(
                goalQueryResponse -> goalQueryResponse.setSubGoalResponses(
                        subGoalQueryResponseMap.get(goalQueryResponse.getId())
                )
        );
        calendarQueryResponse.setGoalResponses(goalQueryResponses);

        return calendarQueryResponse;
    }

    private List<Long> getGoalIds(List<GoalQueryResponse> goalQueryResponses) {
        return goalQueryResponses.stream()
                .map(GoalQueryResponse::getId)
                .collect(Collectors.toList());
    }

}
