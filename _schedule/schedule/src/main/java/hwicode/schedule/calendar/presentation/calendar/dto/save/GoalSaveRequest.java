package hwicode.schedule.calendar.presentation.calendar.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalSaveRequest {

    @NotBlank
    private String goalName;

    @NotEmpty
    @Size(max = 24, message = "YearMonths size must not exceed 24")
    private Set<YearMonth> yearMonths;

    public List<YearMonth> getYearMonths() {
        return new ArrayList<>(yearMonths);
    }
}
