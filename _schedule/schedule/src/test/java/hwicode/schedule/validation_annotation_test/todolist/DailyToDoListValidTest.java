package hwicode.schedule.validation_annotation_test.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.application.DailyToDoListAggregateService;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.DailyToDoListController;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.DAILY_TO_DO_LIST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyToDoListController.class)
class DailyToDoListValidTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DailyToDoListAggregateService dailyToDoListAggregateService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @ParameterizedTest
    @ValueSource(strings = {"좋은데!", "", "  "})
    void 리뷰와_이모지가_존재하면_통과된다(String review) throws Exception {
        // given
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(review, Emoji.GOOD);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information", DAILY_TO_DO_LIST_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dailyToDoListInformationChangeRequest)));

        // then
        perform.andExpect(status().isOk());
    }

}
