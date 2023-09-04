package hwicode.schedule.dailyschedule.daily_schedule_query.infra;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.SubTaskQueryResponse;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class SubTaskQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<SubTaskQueryResponse> findSubTaskQueryResponsesBy(List<Long> taskIds) {
        if (taskIds.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT "
                + "id, name, sub_task_status, task_id "
                + "FROM sub_task s "
                + "WHERE task_id IN (:taskIds) "
                + "ORDER BY s.id ASC";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("taskIds", taskIds);

        return namedParameterJdbcTemplate.query(sql, paramMap, getSubTaskQueryResponseRowMapper());
    }

    private RowMapper<SubTaskQueryResponse> getSubTaskQueryResponseRowMapper() {
        return (rs, rowNum) -> SubTaskQueryResponse.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .subTaskStatus(SubTaskStatus.valueOf(rs.getString("sub_task_status")))
                .taskId(rs.getLong("task_id"))
                .build();
    }

}
