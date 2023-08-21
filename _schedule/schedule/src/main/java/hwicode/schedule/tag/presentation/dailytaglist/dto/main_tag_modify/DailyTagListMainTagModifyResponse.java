package hwicode.schedule.tag.presentation.dailytaglist.dto.main_tag_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyTagListMainTagModifyResponse {

    private Long dailyTagListId;
    private Long tagId;
    private String mainTagName;
}
