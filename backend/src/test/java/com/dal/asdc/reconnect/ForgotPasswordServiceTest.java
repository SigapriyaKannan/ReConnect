package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.exception.EmailSendingException;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ForgotPasswordServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordService forgotPasswordService;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        forgotPasswordService = new ForgotPasswordService(usersRepository, mailSender, passwordEncoder);
    }

    @Test
    void testSendResetEmail_UserExists() {
        Users user = mock(Users.class);
        when(usersRepository.findByUserEmail("test@example.com")).thenReturn(user);

        forgotPasswordService.sendResetEmail("test@example.com");

        verify(usersRepository, times(1)).save(user);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(user, times(1)).setResetToken(anyString());
    }

    @Test
    void testSendResetEmail_UserDoesNotExist() {
        when(usersRepository.findByUserEmail("nonexistent@example.com")).thenReturn(null);

        forgotPasswordService.sendResetEmail("nonexistent@example.com");

        verify(usersRepository, never()).save(any(Users.class));
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testResetPassword_ValidToken() {
        Users user = mock(Users.class);
        when(usersRepository.findByResetToken("valid-token")).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-password");

        boolean result = forgotPasswordService.resetPassword("valid-token", "new-password");

        verify(user).setPassword("hashed-password");
        verify(user).setResetToken(null);
        verify(usersRepository, times(1)).save(user);
        assertTrue(result);
    }

    @Test
    void testResetPassword_InvalidToken() {
        when(usersRepository.findByResetToken("invalid-token")).thenReturn(null);

        boolean result = forgotPasswordService.resetPassword("invalid-token", "new-password");

        verify(usersRepository, never()).save(any(Users.class));
        assertFalse(result);
    }

    @Test
    void testSendEmail_Success() {
        Users user = mock(Users.class);
        when(usersRepository.findByUserEmail("test@example.com")).thenReturn(user);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        forgotPasswordService.sendResetEmail("test@example.com");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_Failure() {
        Users user = mock(Users.class);
        when(usersRepository.findByUserEmail("test@example.com")).thenReturn(user);
        doThrow(new EmailSendingException("Failed to send password reset email", HttpStatus.EXPECTATION_FAILED)).when(mailSender).send(any(SimpleMailMessage.class));

        EmailSendingException exception = assertThrows(EmailSendingException.class, () -> {
            forgotPasswordService.sendResetEmail("test@example.com");
        });

        assertEquals("Failed to send password reset email", exception.getMessage());
        assertEquals(HttpStatus.EXPECTATION_FAILED, exception.getStatus());
    }
}
