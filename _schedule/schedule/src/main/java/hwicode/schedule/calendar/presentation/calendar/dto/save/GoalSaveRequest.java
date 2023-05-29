package hwicode.schedule.calendar.presentation.calendar.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    private Set<YearMonth> yearMonths;

    public List<YearMonth> getYearMonths() {
        return new ArrayList<>(yearMonths);
    }
}
