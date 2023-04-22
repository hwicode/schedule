package hwicode.schedule.dailyschedule.timetable.presentation;

import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LearningTimeController.class)
class LearningTImeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LearningTimeService learningTimeService;

    @Test
    void 학습_시간의_학습_주제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        Long learningTimeId = 1L;

        // when
        ResultActions perform = mockMvc.perform(
                delete(String.format("/dailyschedule/timetable/%s/subject", learningTimeId))
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(learningTimeService).deleteSubject(any());
    }

}
