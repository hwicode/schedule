package hwicode.schedule.auth.infra.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DecodedAccessToken {

    private final Long userId;
}
