package hwicode.schedule.dailyschedule.daily_schedule_query.application;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.infra.DailyScheduleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyScheduleQueryService {

    private final DailyScheduleQueryRepository dailyScheduleQueryRepository;

    @Transactional(readOnly = true)
    public List<DailyScheduleSummaryQueryResponse> getDailyToDoListQueryResponse(YearMonth yearMonth) {
        YearMonth nextMonth = yearMonth.plusMonths(1);
        return dailyScheduleQueryRepository.findDailyScheduleSummaryQueryResponseBy(yearMonth, nextMonth);
    }

}
