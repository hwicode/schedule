package hwicode.schedule.dailyschedule.daily_schedule_query.infra;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.TaskQueryResponse;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TaskQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<TaskQueryResponse> findTaskQueryResponsesBy(Long dailyScheduleId) {
        String sql = "SELECT "
                + "id, name, priority, importance, difficulty, task_status "
                + "FROM task t "
                + "WHERE t.daily_schedule_id = ?";
        return jdbcTemplate.query(sql, getTaskQueryResponseRowMapper(), dailyScheduleId);
    }

    private RowMapper<TaskQueryResponse> getTaskQueryResponseRowMapper() {
        return (rs, rowNum) -> TaskQueryResponse.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .priority(Priority.valueOf(rs.getString("priority")))
                .importance(Importance.valueOf(rs.getString("importance")))
                .difficulty(Difficulty.valueOf(rs.getString("difficulty")))
                .taskStatus(TaskStatus.valueOf(rs.getString("task_status")))
                .build();
    }

}
