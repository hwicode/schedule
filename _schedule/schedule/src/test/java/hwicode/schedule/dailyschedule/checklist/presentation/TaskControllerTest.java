package hwicode.schedule.dailyschedule.checklist.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long DAILY_CHECKLIST_ID = 1L;
    private final Long TASK_ID = 2L;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, "name");
        given(taskService.saveTask(any()))
                .willReturn(TASK_ID);

        // when then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskSaveRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(TASK_ID))
                .andExpect(jsonPath("$.taskName").value("name"));

        verify(taskService).saveTask(any());
    }

    @Test
    void 과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(DAILY_CHECKLIST_ID);

        // when then
        mockMvc.perform(delete("/tasks/taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(any(), any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, Status.DONE);
        given(taskService.changeTaskStatus(any(), any()))
                .willReturn(Status.DONE);

        // when then
        mockMvc.perform(patch("/tasks/taskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("taskName"))
                .andExpect(jsonPath("$.modifiedStatus").value("DONE"));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_어려움_점수의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(DAILY_CHECKLIST_ID, Difficulty.HARD);
        given(taskService.changeTaskDifficulty(any(), any()))
                .willReturn(Difficulty.HARD);

        // when then
        mockMvc.perform(patch("/tasks/taskName/difficulty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDifficultyModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("taskName"))
                .andExpect(jsonPath("$.modifiedDifficulty").value("HARD"));

        verify(taskService).changeTaskDifficulty(any(), any());
    }

}
