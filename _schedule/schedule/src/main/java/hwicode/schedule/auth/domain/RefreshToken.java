package hwicode.schedule.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshToken {

    private String token;
    private long expiryMs;

    public boolean isSameToken(String token) {
        return this.token.equals(token);
    }
}
