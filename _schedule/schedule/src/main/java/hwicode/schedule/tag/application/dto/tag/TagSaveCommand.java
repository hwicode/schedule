package hwicode.schedule.tag.application.dto.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TagSaveCommand {

    private final Long userId;
    private final String name;
}
