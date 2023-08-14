package hwicode.schedule.tag.presentation.memo.dto.text_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoTextModifyResponse {

    private Long memoId;
    private String newText;
}
