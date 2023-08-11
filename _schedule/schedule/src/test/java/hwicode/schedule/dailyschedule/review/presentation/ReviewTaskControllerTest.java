package hwicode.schedule.dailyschedule.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.review.application.ReviewTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewTaskController.class)
class ReviewTaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewTaskService reviewTaskService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 과제의_복습을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskReviewRequest taskReviewRequest = new TaskReviewRequest(REVIEW_CYCLE_ID, START_DATE);
        TaskReviewResponse taskReviewResponse = new TaskReviewResponse(REVIEW_TASK_ID);

        given(reviewTaskService.reviewTask(any(), any(), any()))
                .willReturn(REVIEW_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/tasks/{taskId}/review",
                        REVIEW_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskReviewRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskReviewResponse)
                ));

        verify(reviewTaskService).reviewTask(any(), any(), any());
    }

    @Test
    void 과제의_복습을_취소하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(reviewTaskService.cancelReviewedTask(any()))
                .willReturn(REVIEW_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/tasks/{taskId}/review",
                REVIEW_TASK_ID));

        // then
        perform.andExpect(status().isNoContent());

        verify(reviewTaskService).cancelReviewedTask(any());
    }

}
