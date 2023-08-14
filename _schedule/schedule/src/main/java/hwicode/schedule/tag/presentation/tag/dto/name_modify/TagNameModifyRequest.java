package hwicode.schedule.tag.presentation.tag.dto.name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TagNameModifyRequest {

    @NotBlank
    private String newTagName;
}
