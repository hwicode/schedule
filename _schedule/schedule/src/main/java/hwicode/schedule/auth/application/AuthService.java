package hwicode.schedule.auth.application;

import hwicode.schedule.auth.application.dto.AuthTokenResponse;
import hwicode.schedule.auth.application.dto.ReissuedAuthTokenResponse;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.application.InvalidRefreshTokenException;
import hwicode.schedule.auth.infra.client.OauthClientMapper;
import hwicode.schedule.auth.infra.client.UserInfo;
import hwicode.schedule.auth.infra.other_boundedcontext.UserConnector;
import hwicode.schedule.auth.infra.token.DecodedAccessToken;
import hwicode.schedule.auth.infra.token.RefreshTokenRepository;
import hwicode.schedule.auth.infra.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final OauthClientMapper oauthClientMapper;
    private final UserConnector userConnector;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String getOauthLoginUrl(OauthProvider oauthProvider) {
        return oauthClientMapper.getAuthUrl(oauthProvider);
    }

    public AuthTokenResponse loginWithOauth(OauthProvider oauthProvider, String code) {
        OauthClient oauthClient = oauthClientMapper.getOauthClient(oauthProvider);
        UserInfo userInfo = oauthClient.getUserInfo(code);

        OauthUser oauthUser = userConnector.saveOrUpdate(userInfo.toEntity());

        String accessToken = tokenProvider.createAccessToken(oauthUser);
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);
        refreshTokenRepository.save(oauthUser.getId(), refreshToken);

        return new AuthTokenResponse(accessToken, refreshToken.getToken(), refreshToken.getExpiryMs());
    }

    public ReissuedAuthTokenResponse reissueAccessToken(String accessToken, String refreshToken) {
        DecodedAccessToken decodedAccessToken = tokenProvider.decodeExpiredAccessToken(accessToken);

        Long userId = decodedAccessToken.getUserId();
        RefreshToken savedRefreshToken = refreshTokenRepository.get(userId);

        if (!savedRefreshToken.isSameToken(refreshToken)) {
            refreshTokenRepository.delete(userId);
            throw new InvalidRefreshTokenException();
        }

        OauthUser oauthUser = userConnector.findById(userId);
        String reissuedAccessToken = tokenProvider.createAccessToken(oauthUser);
        RefreshToken reissuedRefreshToken = tokenProvider.createRefreshToken(oauthUser);
        refreshTokenRepository.save(oauthUser.getId(), reissuedRefreshToken);

        return new ReissuedAuthTokenResponse(reissuedAccessToken, reissuedRefreshToken.getToken(), reissuedRefreshToken.getExpiryMs());
    }
}
