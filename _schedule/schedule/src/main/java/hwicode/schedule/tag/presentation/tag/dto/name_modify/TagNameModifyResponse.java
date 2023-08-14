package hwicode.schedule.tag.presentation.tag.dto.name_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagNameModifyResponse {

    private Long tagId;
    private String newTagName;
}
