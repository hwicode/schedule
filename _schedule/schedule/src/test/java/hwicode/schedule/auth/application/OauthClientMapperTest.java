package hwicode.schedule.auth.application;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.infra.client.google.GoogleOauthClient;
import hwicode.schedule.auth.infra.client.google.GoogleProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class OauthClientMapperTest {

    @Test
    void OauthProvider를_통해_OauthClient를_가져올_수_있다() {
        // given
        GoogleProperties googleProperties = mock(GoogleProperties.class);
        OauthClient googleOauthClient = new GoogleOauthClient(googleProperties);
        OauthClientMapper oauthClientMapper = new OauthClientMapper(List.of(googleOauthClient));

        // when
        OauthClient oauthClient = oauthClientMapper.getOauthClient(OauthProvider.GOOGLE);

        // then
        assertThat(oauthClient).isInstanceOf(GoogleOauthClient.class);
    }

}
