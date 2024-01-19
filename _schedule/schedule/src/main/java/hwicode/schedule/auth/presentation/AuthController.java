package hwicode.schedule.auth.presentation;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public void logInWithOauth(@PathVariable OauthProvider oauthProvider, @RequestParam String code) {
        authService.loginWithOauth(oauthProvider, code);
    }
}
