package hwicode.schedule.tag.presentation.memo.dto.save_with_tags;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemoSaveWithTagsResponse {

    private Long dailyTagListId;
    private Long memoId;
    private String text;
    private List<Long> tagIds;
}
