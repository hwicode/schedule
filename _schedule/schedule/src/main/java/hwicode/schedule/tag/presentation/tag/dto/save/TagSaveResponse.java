package hwicode.schedule.tag.presentation.tag.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagSaveResponse {

    private Long tagId;
    private String tagName;
}
