package hwicode.schedule.auth.presentation;

import hwicode.schedule.auth.application.dto.AuthTokenResponse;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth2/{oauthProvider}/login")
    @ResponseStatus(value = HttpStatus.FOUND)
    public void logInOauthUrl(@PathVariable OauthProvider oauthProvider, HttpServletResponse response) {
        String loginUrl = authService.getOauthLoginUrl(oauthProvider);
        response.setHeader(HttpHeaders.LOCATION, loginUrl);
    }

    @GetMapping("/oauth2/{oauthProvider}/callback")
    @ResponseStatus(value = HttpStatus.OK)
    public void logInWithOauth(@PathVariable OauthProvider oauthProvider, @RequestParam String code,
                               HttpServletResponse response) {
        AuthTokenResponse authTokenResponse = authService.loginWithOauth(oauthProvider, code);

        ResponseCookie cookieHeader = ResponseCookie.from("refreshToken", authTokenResponse.getRefreshToken())
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .maxAge(authTokenResponse.getRefreshTokenExpiryMs() / 1000)
                .path("/oauth2")
                .build();
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authTokenResponse.getAccessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader.toString());
    }
}
