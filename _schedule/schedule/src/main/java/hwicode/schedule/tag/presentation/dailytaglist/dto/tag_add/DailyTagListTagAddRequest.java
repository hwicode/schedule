package hwicode.schedule.tag.presentation.dailytaglist.dto.tag_add;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DailyTagListTagAddRequest {

    @NotNull @Positive
    private Long tagId;
}
