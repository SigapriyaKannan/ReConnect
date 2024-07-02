package com.dal.asdc.reconnect;

import com.dal.asdc.reconnect.constants.TestConstants;
import com.dal.asdc.reconnect.exception.EmailSendingException;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import com.dal.asdc.reconnect.service.ForgotPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

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
        when(usersRepository.findByUserEmail(TestConstants.TEST_EMAIL)).thenReturn(user);

        forgotPasswordService.sendResetEmail(TestConstants.TEST_EMAIL);

        verify(usersRepository, times(1)).save(user);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(user, times(1)).setResetToken(anyString());
    }

    @Test
    void testResetPassword_ValidToken() {
        Users user = mock(Users.class);
        when(usersRepository.findByResetToken(TestConstants.VALID_TOKEN)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(TestConstants.HASHED_PASSWORD);

        boolean result = forgotPasswordService.resetPassword(TestConstants.VALID_TOKEN, TestConstants.NEW_PASSWORD);

        verify(user).setPassword(TestConstants.HASHED_PASSWORD);
        verify(user).setResetToken(null);
        verify(usersRepository, times(1)).save(user);
        assertTrue(result);
    }

    @Test
    void testResetPassword_InvalidToken() {
        when(usersRepository.findByResetToken(TestConstants.INVALID_TOKEN)).thenReturn(null);

        boolean result = forgotPasswordService.resetPassword(TestConstants.INVALID_TOKEN, TestConstants.NEW_PASSWORD);

        verify(usersRepository, never()).save(any(Users.class));
        assertFalse(result);
    }

    @Test
    void testSendEmail_Success() {
        Users user = mock(Users.class);
        when(usersRepository.findByUserEmail(TestConstants.TEST_EMAIL)).thenReturn(user);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        forgotPasswordService.sendResetEmail(TestConstants.TEST_EMAIL);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_Failure() {
        // Simulate user not found
        when(usersRepository.findByUserEmail(TestConstants.TEST_EMAIL)).thenReturn(null);

        // Ensure exception is thrown
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            forgotPasswordService.sendResetEmail(TestConstants.TEST_EMAIL);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(TestConstants.USER_NOT_FOUND, exception.getReason());

        // Verify no email is sent
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_EmailSendingFailure() {
        Users user = new Users();
        user.setUserEmail(TestConstants.TEST_EMAIL);
        when(usersRepository.findByUserEmail(TestConstants.TEST_EMAIL)).thenReturn(user);

        // Simulate email sending failure
        doThrow(new EmailSendingException(TestConstants.FAILED_TO_SEND_EMAIL, HttpStatus.EXPECTATION_FAILED)).when(mailSender).send(any(SimpleMailMessage.class));

        EmailSendingException exception = assertThrows(EmailSendingException.class, () -> {
            forgotPasswordService.sendResetEmail(TestConstants.TEST_EMAIL);
        });

        assertEquals(TestConstants.FAILED_TO_SEND_EMAIL, exception.getMessage());
        assertEquals(HttpStatus.EXPECTATION_FAILED, exception.getStatus());

        // Verify user repository interactions
        verify(usersRepository).findByUserEmail(TestConstants.TEST_EMAIL);
        verify(usersRepository).save(user);

        // Verify email sending attempt
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}