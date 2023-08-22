package hwicode.schedule.dailyschedule.daily_schedule_query.infra;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DailyScheduleQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DailyScheduleSummaryQueryResponse> findDailyScheduleSummaryQueryResponseBy(YearMonth start, YearMonth end) {
        String sql = "SELECT "
                + "id, today, total_difficulty_score, today_done_percent, emoji, main_tag_name "
                + "FROM daily_schedule d "
                + "WHERE d.today >= ? AND d.today < ? ";
        return jdbcTemplate.query(sql, getDailyScheduleSummaryQueryResponseRowMapper(), start.atDay(1), end.atDay(1));
    }

    private RowMapper<DailyScheduleSummaryQueryResponse> getDailyScheduleSummaryQueryResponseRowMapper() {
        return (rs, rowNum) -> DailyScheduleSummaryQueryResponse.builder()
                .id(rs.getLong("id"))
                .yearAndMonthAndDay(rs.getTimestamp("today").toLocalDateTime().toLocalDate())
                .totalDifficultyScore(rs.getInt("total_difficulty_score"))
                .todayDonePercent(rs.getInt("today_done_percent"))
                .emoji(Emoji.valueOf(rs.getString("emoji")))
                .mainTagName(rs.getString("main_tag_name"))
                .build();
    }

}
