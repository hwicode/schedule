package hwicode.schedule.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshToken {

    private String token;
    private long expiryMs;
}
