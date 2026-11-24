package com.lazar.courier.shipment.auth;

import com.lazar.courier.shipment.common.ConflictException;
import com.lazar.courier.shipment.security.JwtService;
import com.lazar.courier.shipment.user.Role;
import com.lazar.courier.shipment.user.User;
import com.lazar.courier.shipment.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    // ---------- register ----------

    @Test
    void register_savesSenderWithHashedPassword() {
        // Arrange
        RegisterRequest request = new RegisterRequest("  Marko@Mail.com ", "lozinka123", " Marko Markovic ");
        when(userRepository.existsByEmail("marko@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("lozinka123")).thenReturn("hashed-password");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertThat(saved.getEmail()).isEqualTo("marko@mail.com");
        assertThat(saved.getFullName()).isEqualTo("Marko Markovic");
        assertThat(saved.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(saved.getRole()).isEqualTo(Role.SENDER);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.role()).isEqualTo("SENDER");
    }

    @Test
    void register_throwsConflict_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("marko@mail.com", "lozinka123", "Marko Markovic");
        when(userRepository.existsByEmail("marko@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email is already registered");

        verify(userRepository, never()).save(any());
    }

    // ---------- login ----------

    @Test
    void login_returnsToken_whenCredentialsAreValid() {
        User user = existingUser(true);
        when(userRepository.findByEmail("marko@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("lozinka123", "hashed-password")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest("Marko@Mail.com", "lozinka123"));

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void login_throwsBadCredentials_whenEmailDoesNotExist() {
        when(userRepository.findByEmail("nepostoji@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("nepostoji@mail.com", "lozinka123")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_throwsBadCredentials_whenPasswordIsWrong() {
        User user = existingUser(true);
        when(userRepository.findByEmail("marko@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pogresna", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("marko@mail.com", "pogresna")))
                .isInstanceOf(BadCredentialsException.class);

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_throwsBadCredentials_whenUserIsInactive() {
        User user = existingUser(false);
        when(userRepository.findByEmail("marko@mail.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(new LoginRequest("marko@mail.com", "lozinka123")))
                .isInstanceOf(BadCredentialsException.class);
    }

    // ---------- helper ----------

    private User existingUser(boolean active) {
        User user = new User("marko@mail.com", "hashed-password", "Marko Markovic", Role.SENDER);
        user.setId(1L);
        user.setActive(active);
        return user;
    }
}