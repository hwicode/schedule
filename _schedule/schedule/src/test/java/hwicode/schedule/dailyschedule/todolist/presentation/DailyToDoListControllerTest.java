package hwicode.schedule.dailyschedule.todolist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.todolist.application.DailyToDoListAggregateService;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
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

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.DAILY_TO_DO_LIST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyToDoListController.class)
class DailyToDoListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyToDoListAggregateService dailyToDoListAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 투두리스트의_정보_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        String review = "좋은데!";
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(review, Emoji.GOOD);
        DailyToDoListInformationChangeResponse dailyToDoListInformationChangeResponse = new DailyToDoListInformationChangeResponse(DAILY_TO_DO_LIST_ID, review, Emoji.GOOD);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information", DAILY_TO_DO_LIST_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dailyToDoListInformationChangeRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(dailyToDoListInformationChangeResponse)
                ));

        verify(dailyToDoListAggregateService).changeDailyToDoListInformation(any());
    }

    @Test
    void 투두리스트를_찾을_때_투두리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyToDoListNotExistException dailyToDoListNotExistException = new DailyToDoListNotExistException();
        given(dailyToDoListAggregateService.changeDailyToDoListInformation(any()))
                .willThrow(dailyToDoListNotExistException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information", DAILY_TO_DO_LIST_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new DailyToDoListInformationChangeRequest("좋은데!", Emoji.GOOD)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyToDoListNotExistException.getMessage()));

        verify(dailyToDoListAggregateService).changeDailyToDoListInformation(any());
    }
}
