package hwicode.schedule.dailyschedule.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.review.application.ReviewTaskService;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewCycleNotFoundException;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewTaskNotFoundException;
import hwicode.schedule.dailyschedule.review.presentation.reviewtask.ReviewTaskController;
import hwicode.schedule.dailyschedule.review.presentation.reviewtask.dto.TaskReviewRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewtask.dto.TaskReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewTaskController.class)
class ReviewTaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewTaskService reviewTaskService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 과제의_복습을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskReviewRequest taskReviewRequest = new TaskReviewRequest(REVIEW_CYCLE_ID, START_DATE);
        TaskReviewResponse taskReviewResponse = new TaskReviewResponse(REVIEW_TASK_ID);

        given(reviewTaskService.reviewTask(any()))
                .willReturn(REVIEW_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/tasks/{taskId}/review",
                        REVIEW_TASK_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskReviewRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskReviewResponse)
                ));

        verify(reviewTaskService).reviewTask(any());
    }

    @Test
    void 과제의_복습을_취소하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(reviewTaskService.cancelReviewedTask(any()))
                .willReturn(REVIEW_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/tasks/{taskId}/review",
                REVIEW_TASK_ID)
                .header("Authorization", BEARER + "accessToken"));

        // then
        perform.andExpect(status().isNoContent());

        verify(reviewTaskService).cancelReviewedTask(any());
    }

    @Test
    void 과제의_복습을_요청할_때_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        ReviewTaskNotFoundException reviewTaskNotFoundException = new ReviewTaskNotFoundException();
        given(reviewTaskService.reviewTask(any()))
                .willThrow(reviewTaskNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/tasks/{taskId}/review",
                        REVIEW_TASK_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TaskReviewRequest(REVIEW_CYCLE_ID, START_DATE)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(reviewTaskNotFoundException.getMessage()));

        verify(reviewTaskService).reviewTask(any());
    }

    @Test
    void 과제의_복습을_요청할_때_복습_주기가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        ReviewCycleNotFoundException reviewCycleNotFoundException = new ReviewCycleNotFoundException();
        given(reviewTaskService.reviewTask(any()))
                .willThrow(reviewCycleNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/tasks/{taskId}/review",
                        REVIEW_TASK_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TaskReviewRequest(REVIEW_CYCLE_ID, START_DATE)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(reviewCycleNotFoundException.getMessage()));

        verify(reviewTaskService).reviewTask(any());
    }

}
