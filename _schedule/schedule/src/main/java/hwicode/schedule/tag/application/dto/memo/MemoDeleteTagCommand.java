package hwicode.schedule.tag.application.dto.memo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemoDeleteTagCommand {

    private final Long userId;
    private final Long memoId;
    private final Long tagId;
}
