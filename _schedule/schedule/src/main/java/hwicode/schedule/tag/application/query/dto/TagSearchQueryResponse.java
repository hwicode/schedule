package hwicode.schedule.tag.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class TagSearchQueryResponse {

    private final Long id;
    private final String name;
}
