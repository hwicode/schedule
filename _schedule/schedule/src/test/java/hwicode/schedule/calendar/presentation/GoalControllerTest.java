package hwicode.schedule.calendar.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.calendar.application.GoalAggregateService;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoalStatus;
import hwicode.schedule.calendar.exception.domain.goal.*;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import hwicode.schedule.calendar.presentation.goal.GoalController;
import hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify.GoalStatusModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify.GoalStatusModifyResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_save.SubGoalSaveRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_save.SubGoalSaveResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify.SubGoalStatusModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify.SubGoalStatusModifyResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void 서브_목표_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
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

    @Test
    void 서브_목표의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubGoalNameModifyRequest subGoalNameModifyRequest = new SubGoalNameModifyRequest(SUB_GOAL_NAME, NEW_SUB_GOAL_NAME);
        SubGoalNameModifyResponse subGoalNameModifyResponse = new SubGoalNameModifyResponse(GOAL_ID, NEW_SUB_GOAL_NAME);

        given(goalAggregateService.changeSubGoalName(any(), any(), any()))
                .willReturn(NEW_SUB_GOAL_NAME);

        // when
        ResultActions perform = mockMvc.perform(patch("/goals/{goalId}/sub-goals/{subGoalId}/name", GOAL_ID, SUB_GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subGoalNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subGoalNameModifyResponse)
                ));

        verify(goalAggregateService).changeSubGoalName(any(), any(), any());
    }

    @Test
    void 서브_목표_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(delete("/goals/{goalId}/sub-goals", GOAL_ID)
                .param("subGoalName",SUB_GOAL_NAME));

        // then
        perform.andExpect(status().isNoContent());

        verify(goalAggregateService).deleteSubGoal(any(), any());
    }

    @Test
    void 서브_목표_진행_상태의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubGoalStatusModifyRequest subGoalStatusModifyRequest = new SubGoalStatusModifyRequest(SUB_GOAL_NAME, SubGoalStatus.DONE);
        SubGoalStatusModifyResponse subGoalStatusModifyResponse = new SubGoalStatusModifyResponse(SUB_GOAL_NAME, GoalStatus.PROGRESS, SubGoalStatus.DONE);

        given(goalAggregateService.changeSubGoalStatus(any(), any(), any()))
                .willReturn(GoalStatus.PROGRESS);

        // when
        ResultActions perform = mockMvc.perform(patch("/goals/{goalId}/sub-goals/{subGoalId}/status", GOAL_ID, SUB_GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subGoalStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subGoalStatusModifyResponse)
                ));

        verify(goalAggregateService).changeSubGoalStatus(any(), any(), any());
    }

    @Test
    void 목표의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        GoalStatusModifyRequest goalStatusModifyRequest = new GoalStatusModifyRequest(GoalStatus.DONE);
        GoalStatusModifyResponse goalStatusModifyResponse = new GoalStatusModifyResponse(GOAL_ID, GoalStatus.DONE);

        given(goalAggregateService.changeGoalStatus(any(), any()))
                .willReturn(GoalStatus.DONE);

        // when
        ResultActions perform = mockMvc.perform(patch("/goals/{goalId}/status", GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(goalStatusModifyResponse)
                ));

        verify(goalAggregateService).changeGoalStatus(any(), any());
    }

    @Test
    void 목표의_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(delete("/goals/{goalId}", GOAL_ID));

        // then
        perform.andExpect(status().isNoContent());

        verify(goalAggregateService).deleteGoal(any());
    }

    @Test
    void 목표의_삭제를_요청할_때_목표가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        GoalNotFoundException goalNotFoundException = new GoalNotFoundException();
        given(goalAggregateService.deleteGoal(any()))
                .willThrow(goalNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/goals/{goalId}", GOAL_ID));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(goalNotFoundException.getMessage()));
    }

    @Test
    void 목표의_진행_상태_변경을_요청할_때_존재하지_않는_진행_상태면_에러가_발생한다() throws Exception {
        // given
        GoalStatusNotFoundException goalStatusNotFoundException = new GoalStatusNotFoundException();
        given(goalAggregateService.changeGoalStatus(any(), any()))
                .willThrow(goalStatusNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/goals/{goalId}/status", GOAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                // 존재하지 않는 진행상태로 가정
                                new GoalStatusModifyRequest(GoalStatus.DONE))
                        ));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(goalStatusNotFoundException.getMessage()));
    }

    @Test
    void 서브_목표_생성을_요청할_때_서브_목표가_중복되면_에러가_발생한다() throws Exception {
        // given
        SubGoalDuplicateException subGoalDuplicateException = new SubGoalDuplicateException();
        given(goalAggregateService.saveSubGoal(any(), any()))
                .willThrow(subGoalDuplicateException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/goals/{goalId}/sub-goals", GOAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubGoalSaveRequest(SUB_GOAL_NAME)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subGoalDuplicateException.getMessage()));
    }

    @Test
    void 목표의_진행_상태를_DONE으로_변경을_요청할_때_서브_목표가_모두_DONE이_아니면_에러가_발생한다() throws Exception {
        // given
        SubGoalNotAllDoneException subGoalNotAllDoneException = new SubGoalNotAllDoneException();
        given(goalAggregateService.changeGoalStatus(any(), any()))
                .willThrow(subGoalNotAllDoneException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/goals/{goalId}/status", GOAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new GoalStatusModifyRequest(GoalStatus.DONE)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subGoalNotAllDoneException.getMessage()));
    }

    @Test
    void 목표의_진행_상태를_TODO로_변경을_요청할_때_서브_목표가_모두_TODO가_아니면_에러가_발생한다() throws Exception {
        // given
        SubGoalNotAllTodoException subGoalNotAllTodoException = new SubGoalNotAllTodoException();
        given(goalAggregateService.changeGoalStatus(any(), any()))
                .willThrow(subGoalNotAllTodoException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/goals/{goalId}/status", GOAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new GoalStatusModifyRequest(GoalStatus.TODO)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subGoalNotAllTodoException.getMessage()));
    }

    @Test
    void 서브_목표의_삭제를_요청할_때_서브_목표가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubGoalNotFoundException subGoalNotFoundException = new SubGoalNotFoundException();
        given(goalAggregateService.deleteSubGoal(any(), any()))
                .willThrow(subGoalNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(delete("/goals/{goalId}/sub-goals", GOAL_ID)
                .param("subGoalName",SUB_GOAL_NAME));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subGoalNotFoundException.getMessage()));
    }

}
