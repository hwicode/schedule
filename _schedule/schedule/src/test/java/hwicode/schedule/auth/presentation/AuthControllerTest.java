package hwicode.schedule.auth.presentation;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static hwicode.schedule.auth.AuthDataHelper.AUTH_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Test
    void 캘린더_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        given(authService.getOauthLoginUrl(any()))
                .willReturn(AUTH_URL);

        // when
        ResultActions perform = mockMvc.perform(
                get("/oauth2/{oauthProvider}/login", OauthProvider.GOOGLE.name())
        );

        // then
        perform.andExpect(status().isFound())
                .andExpect(redirectedUrl((AUTH_URL)));

        verify(authService).getOauthLoginUrl(any());
    }

    @Test
    void 존재하지_않는_OauthProvider를_요청하면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                get("/oauth2/{oauthProvider}/login", "aaa")
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

}
