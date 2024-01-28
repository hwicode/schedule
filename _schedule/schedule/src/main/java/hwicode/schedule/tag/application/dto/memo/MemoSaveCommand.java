package hwicode.schedule.tag.application.dto.memo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemoSaveCommand {

    private final Long userId;
    private final Long dailyTagListId;
    private final String text;
}
