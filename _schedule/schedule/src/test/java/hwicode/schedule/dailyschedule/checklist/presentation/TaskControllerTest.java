package hwicode.schedule.dailyschedule.checklist.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long DAILY_CHECKLIST_ID = 1L;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, "name");
        given(taskService.saveTask(any()))
                .willReturn(DAILY_CHECKLIST_ID);

        // when then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskSaveRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        String.valueOf(DAILY_CHECKLIST_ID)));
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
    }

}

@RestController
class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long saveTask(@RequestBody TaskSaveRequest taskSaveRequest) {
        return taskService.saveTask(taskSaveRequest);
    }

    @DeleteMapping("/tasks/{taskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String taskName, @RequestBody TaskDeleteRequest taskDeleteRequest) {
        taskService.deleteTask(taskDeleteRequest.getDailyChecklistId(), taskName);
    }

    @PatchMapping("/tasks/{taskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable String taskName, @RequestBody TaskStatusModifyRequest taskStatusModifyRequest) {
        Status modifiedStatus = taskService.changeTaskStatus(taskName, taskStatusModifyRequest);
        return new TaskStatusModifyResponse(taskName, modifiedStatus);
    }

    @PatchMapping("/tasks/{taskName}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable String taskName, @RequestBody TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskService.changeTaskDifficulty(taskName, taskDifficultyModifyRequest);
        return new TaskDifficultyModifyResponse(taskName, modifiedDifficulty);
    }

}
