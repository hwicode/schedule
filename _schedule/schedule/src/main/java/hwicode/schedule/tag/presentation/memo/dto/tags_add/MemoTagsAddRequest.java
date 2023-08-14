package hwicode.schedule.tag.presentation.memo.dto.tags_add;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemoTagsAddRequest {

    @NotEmpty
    @Size(min = 1, max = 10, message = "Tag ids is greater than or equal to 1 and less than or equal to 10 (1 <= review cycle <= 10)")
    private Set<Long> tagIds;

    public List<Long> getTagIds() {
        List<Long> list = new ArrayList<>(tagIds);
        Collections.sort(list);
        return list;
    }
}
