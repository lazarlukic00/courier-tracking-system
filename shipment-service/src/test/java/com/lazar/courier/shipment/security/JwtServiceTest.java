package com.lazar.courier.shipment.security;

import com.lazar.courier.shipment.user.Role;
import com.lazar.courier.shipment.user.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "bXktc3VwZXItc2VjcmV0LWRldi1rZXktZm9yLWNvdXJpZXItdHJhY2tpbmc=";

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 60);

        user = new User("marko@mail.com", "hash", "Marko Markovic", Role.SENDER);
        user.setId(1L);
    }

    @Test
    void generateToken_containsUserData() {
        String token = jwtService.generateToken(user);

        Claims claims = jwtService.parseToken(token);

        assertThat(claims.getSubject()).isEqualTo("marko@mail.com");
        assertThat(((Number) claims.get("userId")).longValue()).isEqualTo(1L);
        assertThat(claims.get("role", String.class)).isEqualTo("SENDER");
    }

    @Test
    void isValid_returnsTrue_forFreshToken() {
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isValid(token)).isTrue();
    }

    @Test
    void isValid_returnsFalse_whenTokenIsTampered() {
        String token = jwtService.generateToken(user);
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertThat(jwtService.isValid(tampered)).isFalse();
    }

    @Test
    void isValid_returnsFalse_whenTokenIsExpired() {
        JwtService expiredJwtService = new JwtService(SECRET, -1);
        String expiredToken = expiredJwtService.generateToken(user);

        assertThat(jwtService.isValid(expiredToken)).isFalse();
    }

    @Test
    void isValid_returnsFalse_whenTokenIsSignedWithDifferentKey() {
        String otherSecret = "YW5vdGhlci1zZWNyZXQta2V5LXRoYXQtaXMtbG9uZy1lbm91Z2gtMTIz";
        String foreignToken = new JwtService(otherSecret, 60).generateToken(user);

        assertThat(jwtService.isValid(foreignToken)).isFalse();
    }
}