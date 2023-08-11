package hwicode.schedule.dailyschedule.review.presentation.reviewtask.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskReviewRequest {

    @NotNull @Positive
    private Long reviewCycleId;

    @NotNull
    private LocalDate startDate;
}
