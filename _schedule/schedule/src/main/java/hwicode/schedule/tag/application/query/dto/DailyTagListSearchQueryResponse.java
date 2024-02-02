package hwicode.schedule.tag.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
public class DailyTagListSearchQueryResponse {

    private final Long id;
    private final LocalDate yearAndMonthAndDay;
    private final String mainTagName;
    private final Long userId;
}
