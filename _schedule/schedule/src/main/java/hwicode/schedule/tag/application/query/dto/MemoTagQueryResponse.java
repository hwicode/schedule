package hwicode.schedule.tag.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class MemoTagQueryResponse {

    private final Long tagId;
    private final String tagName;
    private final Long memoId;
}
