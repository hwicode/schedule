package hwicode.schedule.auth.presentation;

import hwicode.schedule.auth.application.dto.AuthTokenResponse;
import hwicode.schedule.auth.application.dto.ReissuedAuthTokenResponse;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.application.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static hwicode.schedule.auth.AuthDataHelper.AUTH_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Test
    void Oauth_Provider의_로그인_페이지를_요청하면_302코드가_리턴된다() throws Exception {
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
    void Oauth_Provider의_로그인_페이지를_요청할_때_Oauth_Provider가_존재하지_않으면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                get("/oauth2/{oauthProvider}/login", "aaa")
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    void Oauth_Provider을_통해_로그인을_요청하면_200코드가_리턴된다() throws Exception {
        // given
        AuthTokenResponse authTokenResponse = new AuthTokenResponse("accessToken", "refreshToken", 1000);
        ResponseCookie cookieHeader = ResponseCookie.from("refreshToken", authTokenResponse.getRefreshToken())
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .maxAge(authTokenResponse.getRefreshTokenExpiryMs() / 1000)
                .path("/auth")
                .build();

        given(authService.loginWithOauth(any(), any()))
                .willReturn(authTokenResponse);

        // when
        ResultActions perform = mockMvc.perform(
                get("/oauth2/{oauthProvider}/callback", OauthProvider.GOOGLE.name())
                        .param("code", "code")
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + authTokenResponse.getAccessToken()))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, cookieHeader.toString()));

        verify(authService).loginWithOauth(any(), any());
    }

    @Test
    void Oauth_Provider을_통해_로그인을_요청할_때_Oauth_Provider가_존재하지_않으면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                get("/oauth2/{oauthProvider}/callback", "aaa")
                        .param("code", "code")
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    void Oauth_Provider을_통해_로그인을_요청할_때_code가_존재하지_않으면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                get("/oauth2/{oauthProvider}/callback", OauthProvider.GOOGLE.name())
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    void Auth_토큰의_재발급을_요청하면_200코드가_리턴된다() throws Exception {
        // given
        ReissuedAuthTokenResponse reissuedAuthTokenResponse = new ReissuedAuthTokenResponse("accessToken", "refreshToken", 1000);
        ResponseCookie cookieHeader = ResponseCookie.from("refreshToken", reissuedAuthTokenResponse.getRefreshToken())
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .maxAge(reissuedAuthTokenResponse.getRefreshTokenExpiryMs() / 1000)
                .path("/auth")
                .build();

        given(authService.reissueAuthToken(any()))
                .willReturn(reissuedAuthTokenResponse);

        // when
        ResultActions perform = mockMvc.perform(
                post("/auth/token")
                        .cookie(new javax.servlet.http.Cookie("refreshToken", "refreshToken"))
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedAuthTokenResponse.getAccessToken()))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, cookieHeader.toString()));

        verify(authService).reissueAuthToken(any());
    }

    @Test
    void Auth_토큰의_재발급을_요청할_때_refreshToken_cookie가_존재하지_않으면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                post("/auth/token")
                        .header("Authorization", "Bearer " + "accessToken")
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    void 로그아웃을_요청하면_200코드가_리턴된다() throws Exception {
        // given
        String cookie = "refreshToken=refreshToken; Path=/auth; Max-Age=0; Expires=Thu, 1 Jan 1970 00:00:00 GMT; HttpOnly; SameSite=Strict";

        given(authService.logout(any()))
                .willReturn(true);

        // when
        ResultActions perform = mockMvc.perform(
                post("/auth/logout")
                        .header("Authorization", "Bearer " + "accessToken")
                        .cookie(new javax.servlet.http.Cookie("refreshToken", "refreshToken"))
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, cookie))
                .andReturn();

        verify(authService).logout(any());
    }

    @Test
    void 로그아웃을_요청할_때_Authentication이_존재하지_않으면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                post("/auth/logout")
                        .cookie(new javax.servlet.http.Cookie("refreshToken", "refreshToken"))
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    void 로그아웃을_요청할_때_refreshToken_cookie가_존재하지_않으면_에러가_발생한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                post("/auth/logout")
                        .header("Authorization", "Bearer " + "accessToken")
        );

        // then
        perform.andExpect(status().isBadRequest());
    }

}
