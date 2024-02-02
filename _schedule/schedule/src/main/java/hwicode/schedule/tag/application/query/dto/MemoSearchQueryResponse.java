package hwicode.schedule.tag.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class MemoSearchQueryResponse {

    private final Long id;
    private final String text;
    private final Long userId;
}
