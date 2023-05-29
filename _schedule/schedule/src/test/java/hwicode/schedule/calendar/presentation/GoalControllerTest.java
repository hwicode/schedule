package hwicode.schedule.calendar.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.calendar.application.GoalAggregateService;
import hwicode.schedule.calendar.presentation.goal.GoalController;
import hwicode.schedule.calendar.presentation.goal.dto.save.SubGoalSaveRequest;
import hwicode.schedule.calendar.presentation.goal.dto.save.SubGoalSaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GoalAggregateService goalAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 목표_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        SubGoalSaveRequest subGoalSaveRequest = new SubGoalSaveRequest(SUB_GOAL_NAME);
        SubGoalSaveResponse subGoalSaveResponse = new SubGoalSaveResponse(GOAL_ID, SUB_GOAL_NAME);

        given(goalAggregateService.saveSubGoal(any(), any()))
                .willReturn(SUB_GOAL_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/goals/{goalId}/sub-goals", GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subGoalSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subGoalSaveResponse)
                ));

        verify(goalAggregateService).saveSubGoal(any(), any());
    }

}
