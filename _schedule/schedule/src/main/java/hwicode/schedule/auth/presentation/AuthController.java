package hwicode.schedule.auth.presentation;

import hwicode.schedule.auth.application.dto.AuthTokenResponse;
import hwicode.schedule.auth.application.dto.ReissuedAuthTokenResponse;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.application.AuthService;
import hwicode.schedule.common.config.auth.LoginInfo;
import hwicode.schedule.common.config.auth.LoginUser;
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

    private static final String BEARER = "Bearer ";
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

        ResponseCookie cookie = makeRefreshTokenCookie(authTokenResponse.getRefreshToken(), authTokenResponse.getRefreshTokenExpiryMs());
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + authTokenResponse.getAccessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/auth/token")
    @ResponseStatus(value = HttpStatus.OK)
    public void reissueAuthToken(@CookieValue(value = "refreshToken") String refreshToken,
                                 HttpServletResponse response) {
        ReissuedAuthTokenResponse reissuedAuthTokenResponse = authService.reissueAuthToken(refreshToken);

        ResponseCookie cookie = makeRefreshTokenCookie(reissuedAuthTokenResponse.getRefreshToken(), reissuedAuthTokenResponse.getRefreshTokenExpiryMs());
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + reissuedAuthTokenResponse.getAccessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(@LoginUser LoginInfo loginInfo,
                       @CookieValue(value = "refreshToken") String refreshToken,
                       HttpServletResponse response) {
        authService.logout(refreshToken);

        ResponseCookie cookie = makeRefreshTokenCookie(refreshToken, 0);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private ResponseCookie makeRefreshTokenCookie(String refreshToken, long refreshTokenExpiryMs) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .maxAge(refreshTokenExpiryMs / 1000)
                .path("/auth")
                .build();
    }
}
