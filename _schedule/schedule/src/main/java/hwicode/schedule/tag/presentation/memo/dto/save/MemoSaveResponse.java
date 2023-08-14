package hwicode.schedule.tag.presentation.memo.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoSaveResponse {

    private Long dailyTagListId;
    private Long memoId;
    private String text;
}
