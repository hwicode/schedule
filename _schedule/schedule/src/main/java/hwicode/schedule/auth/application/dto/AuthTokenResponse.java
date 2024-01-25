package hwicode.schedule.auth.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthTokenResponse {

    private final String accessToken;
    private final String refreshToken;
    private final long refreshTokenExpiryMs;
}
