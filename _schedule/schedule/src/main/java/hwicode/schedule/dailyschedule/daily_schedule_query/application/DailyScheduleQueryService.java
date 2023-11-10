package hwicode.schedule.dailyschedule.daily_schedule_query.application;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.SubTaskQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.TaskQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.exception.DailyScheduleNotExistException;
import hwicode.schedule.dailyschedule.daily_schedule_query.infra.DailyScheduleQueryRepository;
import hwicode.schedule.dailyschedule.daily_schedule_query.infra.SubTaskQueryRepository;
import hwicode.schedule.dailyschedule.daily_schedule_query.infra.TaskQueryRepository;
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
public class DailyScheduleQueryService {

    private final DailyScheduleQueryRepository dailyScheduleQueryRepository;
    private final TaskQueryRepository taskQueryRepository;
    private final SubTaskQueryRepository subTaskQueryRepository;

    @Transactional(readOnly = true)
    public List<DailyScheduleSummaryQueryResponse> getMonthlyDailyScheduleQueryResponses(YearMonth yearMonth) {
        YearMonth nextMonth = yearMonth.plusMonths(1);
        return dailyScheduleQueryRepository.findMonthlyDailyScheduleQueryResponseBy(yearMonth, nextMonth);
    }

    @Transactional(readOnly = true)
    public DailyScheduleQueryResponse getDailyScheduleQueryResponse(Long dailyScheduleId) {
        DailyScheduleQueryResponse dailyScheduleQueryResponse = dailyScheduleQueryRepository.findDailyScheduleQueryResponseBy(dailyScheduleId)
                .orElseThrow(DailyScheduleNotExistException::new);
        List<TaskQueryResponse> taskQueryResponses = taskQueryRepository.findTaskQueryResponsesBy(dailyScheduleId);

        List<Long> taskIds = getTaskIds(taskQueryResponses);
        Map<Long, List<SubTaskQueryResponse>> subTaskQueryResponseMap = subTaskQueryRepository.findSubTaskQueryResponsesBy(taskIds)
                .stream()
                .collect(groupingBy(SubTaskQueryResponse::getTaskId));

        taskQueryResponses.forEach(
                taskQueryResponse -> taskQueryResponse.setSubTaskQueryResponses(
                        subTaskQueryResponseMap.get(taskQueryResponse.getId())
                )
        );
        dailyScheduleQueryResponse.setTaskQueryResponses(taskQueryResponses);
        return dailyScheduleQueryResponse;
    }

    private List<Long> getTaskIds(List<TaskQueryResponse> taskQueryResponses) {
        return taskQueryResponses.stream()
                .map(TaskQueryResponse::getId)
                .collect(Collectors.toList());
    }

}
