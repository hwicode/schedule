package hwicode.schedule.tag.presentation.memo.dto.save_with_tags;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemoSaveWithTagsRequest {

    @NotNull @Positive
    private Long dailyTagListId;

    @NotBlank
    private String text;

    @NotEmpty
    @Size(min = 1, max = 10, message = "Tag ids is greater than or equal to 1 and less than or equal to 10 (1 <= review cycle <= 10)")
    private Set<Long> tagIds;

    public List<Long> getTagIds() {
        List<Long> list = new ArrayList<>(tagIds);
        Collections.sort(list);
        return list;
    }
}
