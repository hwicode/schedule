package hwicode.schedule.auth.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.application.OauthClient;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.infra.client.OauthClientMapper;
import hwicode.schedule.auth.infra.other_boundedcontext.UserConnector;
import hwicode.schedule.auth.infra.token.RefreshTokenRepository;
import hwicode.schedule.auth.infra.token.TokenProvider;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.Cookie;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;

import static hwicode.schedule.auth.AuthDataHelper.AUTH_URL;
import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthEndToEndTest {

    @LocalServerPort
    int port;

    @MockBean
    OauthClientMapper oauthClientMapper;

    @Autowired
    UserConnector userConnector;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
        redisTemplate.getConnectionFactory().getConnection()
                .serverCommands().flushAll();
    }

    @Test
    void Google_로그인_페이지_요청() {
        // given
        OauthClient client = mock(OauthClient.class);
        BDDMockito.when(client.getAuthUrl()).thenReturn(AUTH_URL);
        BDDMockito.when(oauthClientMapper.getOauthClient(any())).thenReturn(client);

        RequestSpecification requestSpecification = given().port(port)
                .redirects().follow(false);

        // when
        Response response = requestSpecification.when()
                .get("/oauth2/{oauthProvider}/login", OauthProvider.GOOGLE);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Google_Oauth로_로그인_요청() {
        // given
        OauthUser oauthUser = new OauthUser("name", "email", OauthProvider.GOOGLE);
        OauthClient client = mock(OauthClient.class);
        BDDMockito.when(client.getUserInfo(any())).thenReturn(oauthUser);
        BDDMockito.when(oauthClientMapper.getOauthClient(any())).thenReturn(client);

        RequestSpecification requestSpecification = given().port(port)
                .queryParam("code", "code");

        // when
        Response response = requestSpecification.when()
                .get("/oauth2/{oauthProvider}/callback", OauthProvider.GOOGLE);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Refresh_토큰_재발급_요청() {
        // given
        OauthUser oauthUser = userConnector.saveOrUpdate(new OauthUser("name", "email", OauthProvider.GOOGLE));

        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);
        refreshTokenRepository.save(oauthUser.getId(), refreshToken);

        String cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .maxAge(10000)
                .path("/auth")
                .build()
                .toString();

        RequestSpecification requestSpecification = given().port(port)
                .cookie(cookie);

        // when
        Response response = requestSpecification.when()
                .post("/auth/token");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 로그아웃_요청() {
        // given
        OauthUser oauthUser = userConnector.saveOrUpdate(new OauthUser("name", "email", OauthProvider.GOOGLE));

        String accessToken = tokenProvider.createAccessToken(oauthUser);
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);
        refreshTokenRepository.save(oauthUser.getId(), refreshToken);

        String cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .maxAge(10000)
                .path("/auth")
                .build()
                .toString();

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .cookie(cookie);

        // when
        Response response = requestSpecification.when()
                .post("/auth/logout");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
