package hwicode.schedule.tag.application.dto.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TagModifyNameCommand {

    private final Long userId;
    private final Long tagId;
    private final String newName;
}
