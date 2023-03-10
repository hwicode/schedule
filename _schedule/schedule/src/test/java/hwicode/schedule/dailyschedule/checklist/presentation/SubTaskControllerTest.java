package hwicode.schedule.dailyschedule.checklist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotFoundException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyRequest;
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

@WebMvcTest(SubTaskController.class)
public class SubTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubTaskService subTaskService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long DAILY_CHECKLIST_ID = 1L;
    private final Long SUB_TASK_ID = 2L;
    private final String TASK_NAME = "taskName";
    private final String SUB_TASK_NAME = "subTaskName";

    @Test
    void 서브_과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME, SUB_TASK_NAME);
        given(subTaskService.saveSubTask(any()))
                .willReturn(SUB_TASK_ID);

        // when then
        mockMvc.perform(post("/subtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskSaveRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subTaskId").value(SUB_TASK_ID))
                .andExpect(jsonPath("$.subTaskName").value(SUB_TASK_NAME));

        verify(subTaskService).saveSubTask(any());
    }

    @Test
    void 서브_과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(DAILY_CHECKLIST_ID, TASK_NAME);

        // when then
        mockMvc.perform(delete("/subtasks/subTaskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(subTaskService).deleteSubTask(any(), any());
    }

    @Test
    void 서브_과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, Status.DONE);
        given(subTaskService.changeSubTaskStatus(any(), any()))
                .willReturn(Status.DONE);

        // when then
        mockMvc.perform(patch("/subtasks/subTaskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskStatusModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subTaskName").value("subTaskName"))
                .andExpect(jsonPath("$.modifiedStatus").value("DONE"));

        verify(subTaskService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 서브_과제_생성을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        SubTaskNameDuplicationException subTaskNameDuplicationException = new SubTaskNameDuplicationException();
        given(subTaskService.saveSubTask(any()))
                .willThrow(subTaskNameDuplicationException);

        // when then
        mockMvc.perform(post("/subtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SubTaskSaveRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskNameDuplicationException.getMessage()));

        verify(subTaskService).saveSubTask(any());
    }

    @Test
    void 서브_과제를_찾을_때_서브_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubTaskNotFoundException subTaskNotFoundException = new SubTaskNotFoundException();
        given(subTaskService.changeSubTaskStatus(any(), any()))
                .willThrow(subTaskNotFoundException);

        // when then
        mockMvc.perform(patch("/subtasks/subTaskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SubTaskStatusModifyRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subTaskNotFoundException.getMessage()));

        verify(subTaskService).changeSubTaskStatus(any(), any());
    }
}
