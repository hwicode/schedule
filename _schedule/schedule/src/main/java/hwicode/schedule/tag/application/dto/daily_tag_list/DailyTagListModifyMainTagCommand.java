package hwicode.schedule.tag.application.dto.daily_tag_list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DailyTagListModifyMainTagCommand {

    private final Long userId;
    private final Long dailyTagListId;
    private final Long tagId;
}
