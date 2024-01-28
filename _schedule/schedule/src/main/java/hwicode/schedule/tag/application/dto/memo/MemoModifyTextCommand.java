package hwicode.schedule.tag.application.dto.memo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemoModifyTextCommand {

    private final Long userId;
    private final Long memoId;
    private final String text;
}
