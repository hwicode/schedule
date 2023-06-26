package hwicode.schedule.dailyschedule.todolist;

import java.time.LocalDate;

public class ToDoListDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long DAILY_TO_DO_LIST_ID = 1L;
    public static final Long TASK_ID = 2L;
    public static final Long SUB_TASK_ID = 3L;

    // given절에서 사용됨, 테스트 중간에 사용되면 이미 존재하는 Task나 SubTask임
    public static final String TASK_NAME = "taskName";
    public static final String SUB_TASK_NAME = "subTaskName";

    public static final String EXTERNAL_MESSAGE = "externalMessage";

    public static final String REVIEW_CYCLE_NAME = "reviewCycleName";
    public static final String NEW_REVIEW_CYCLE_NAME = "newReviewCycleName";

    public static final LocalDate START_DATE = LocalDate.of(2023, 6, 26);
}
