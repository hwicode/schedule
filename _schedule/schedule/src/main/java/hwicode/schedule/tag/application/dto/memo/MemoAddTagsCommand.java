package hwicode.schedule.tag.application.dto.memo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemoAddTagsCommand {

    private final Long userId;
    private final Long memoId;
    private final List<Long> tagIds;
}
