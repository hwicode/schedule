package hwicode.schedule.dailyschedule.todolist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.todolist.application.DailyToDoListService;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.exception.application.DailyToDoListNotExistException;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.DailyToDoListController;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.DAILY_TO_DO_LIST_ID;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.createDailyToDoListInformationChangeRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyToDoListController.class)
class DailyToDoListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyToDoListService dailyToDoListService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @Test
    void 투두리스트의_정보_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        String review = "좋은데!";
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = createDailyToDoListInformationChangeRequest(review, Emoji.GOOD);
        DailyToDoListInformationChangeResponse dailyToDoListInformationChangeResponse = new DailyToDoListInformationChangeResponse(DAILY_TO_DO_LIST_ID, review, Emoji.GOOD);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/todolist/dailytodolists/%s/information", DAILY_TO_DO_LIST_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dailyToDoListInformationChangeRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(dailyToDoListInformationChangeResponse)
                ));

        verify(dailyToDoListService).changeDailyToDoListInformation(any(), any());
    }

    @Test
    void 투두리스트를_찾을_때_투두리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyToDoListNotExistException dailyToDoListNotExistException = new DailyToDoListNotExistException();
        given(dailyToDoListService.changeDailyToDoListInformation(any(), any()))
                .willThrow(dailyToDoListNotExistException);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/todolist/dailytodolists/%s/information", DAILY_TO_DO_LIST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createDailyToDoListInformationChangeRequest("좋은데!", Emoji.GOOD)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyToDoListNotExistException.getMessage()));

        verify(dailyToDoListService).changeDailyToDoListInformation(any(), any());
    }
}