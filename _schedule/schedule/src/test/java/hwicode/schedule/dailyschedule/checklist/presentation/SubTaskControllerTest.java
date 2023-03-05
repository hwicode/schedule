package hwicode.schedule.dailyschedule.checklist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}

@RestController
class SubTaskController {

    private final SubTaskService subTaskService;


    public SubTaskController(SubTaskService subTaskService) {
        this.subTaskService = subTaskService;
    }

    @PostMapping("/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@RequestBody SubTaskSaveRequest subTaskSaveRequest) {
        Long subTaskId = subTaskService.saveSubTask(subTaskSaveRequest);
        return new SubTaskSaveResponse(subTaskId, subTaskSaveRequest.getSubTaskName());
    }
}
