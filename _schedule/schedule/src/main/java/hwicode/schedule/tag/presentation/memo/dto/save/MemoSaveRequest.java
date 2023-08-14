package hwicode.schedule.tag.presentation.memo.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemoSaveRequest {

    @NotNull @Positive
    private Long dailyTagListId;

    @NotBlank
    private String text;
}
