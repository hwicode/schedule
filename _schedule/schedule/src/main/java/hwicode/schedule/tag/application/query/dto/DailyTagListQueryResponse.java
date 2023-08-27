package hwicode.schedule.tag.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
public class DailyTagListQueryResponse {

    private final Long id;
    private final LocalDate yearAndMonthAndDay;
    private final String mainTagName;
}
