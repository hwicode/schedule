package hwicode.schedule.auth.presentation;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth2/{oauthProvider}/login")
    @ResponseStatus(value = HttpStatus.FOUND)
    public void loginPageUrl(@PathVariable OauthProvider oauthProvider, HttpServletResponse response) {
        String authUrl = authService.getAuthUrl(oauthProvider);
        response.setHeader(HttpHeaders.LOCATION, authUrl);
    }
}
