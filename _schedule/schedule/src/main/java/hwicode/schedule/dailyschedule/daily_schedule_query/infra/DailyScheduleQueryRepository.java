package hwicode.schedule.dailyschedule.daily_schedule_query.infra;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DailyScheduleQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DailyScheduleSummaryQueryResponse> findMonthlyDailyScheduleQueryResponseBy(YearMonth start, YearMonth end) {
        String sql = "SELECT "
                + "id, today, total_difficulty_score, today_done_percent, emoji, main_tag_name "
                + "FROM daily_schedule d "
                + "WHERE d.today >= ? AND d.today < ? "
                + "ORDER BY d.today ASC";
        return jdbcTemplate.query(sql, getMonthlyDailyScheduleQueryResponseRowMapper(), start.atDay(1), end.atDay(1));
    }

    private RowMapper<DailyScheduleSummaryQueryResponse> getMonthlyDailyScheduleQueryResponseRowMapper() {
        return (rs, rowNum) -> DailyScheduleSummaryQueryResponse.builder()
                .id(rs.getLong("id"))
                .yearAndMonthAndDay(rs.getTimestamp("today").toLocalDateTime().toLocalDate())
                .totalDifficultyScore(rs.getInt("total_difficulty_score"))
                .todayDonePercent(rs.getInt("today_done_percent"))
                .emoji(Emoji.valueOf(rs.getString("emoji")))
                .mainTagName(rs.getString("main_tag_name"))
                .build();
    }

    public Optional<DailyScheduleQueryResponse> findDailyScheduleQueryResponseBy(Long dailyScheduleId) {
        String sql = "SELECT "
                + "id, today, total_difficulty_score, today_done_percent, total_learning_time, emoji, main_tag_name, review "
                + "FROM daily_schedule d "
                + "WHERE d.id = ?";
        try {
            DailyScheduleQueryResponse dailyScheduleQueryResponse = jdbcTemplate.queryForObject(sql, getDailyScheduleQueryResponseRowMapper(), dailyScheduleId);
            return Optional.ofNullable(dailyScheduleQueryResponse);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<DailyScheduleQueryResponse> getDailyScheduleQueryResponseRowMapper() {
        return (rs, rowNum) -> DailyScheduleQueryResponse.builder()
                .id(rs.getLong("id"))
                .yearAndMonthAndDay(rs.getTimestamp("today").toLocalDateTime().toLocalDate())
                .totalDifficultyScore(rs.getInt("total_difficulty_score"))
                .todayDonePercent(rs.getInt("today_done_percent"))
                .totalLearningTime(rs.getInt("total_learning_time"))
                .emoji(Emoji.valueOf(rs.getString("emoji")))
                .mainTagName(rs.getString("main_tag_name"))
                .review(rs.getString("review"))
                .build();
    }

}
