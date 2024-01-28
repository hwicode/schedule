package hwicode.schedule.tag.application.dto.memo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemoDeleteCommand {

    private final Long userId;
    private final Long memoId;
}
