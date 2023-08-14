package hwicode.schedule.tag.presentation.memo.dto.tags_add;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemoTagsAddResponse {

    private Long memoId;
    private List<Long> tagIds;
}
