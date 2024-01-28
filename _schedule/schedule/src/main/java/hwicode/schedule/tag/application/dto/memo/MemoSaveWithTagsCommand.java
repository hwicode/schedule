package hwicode.schedule.tag.application.dto.memo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemoSaveWithTagsCommand {

    private final Long userId;
    private final Long dailyTagListId;
    private final List<Long> tagIds;
    private final String text;
}
