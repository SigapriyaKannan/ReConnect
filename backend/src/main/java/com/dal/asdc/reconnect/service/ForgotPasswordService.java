package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    public void sendResetEmail(String email) {
        Users user = usersRepository.findByUserEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            usersRepository.save(user);
            
            String resetUrl = resetPasswordUrl + token;
            String subject = "Password Reset Request";
            String message = "To reset your password, click the link below:\n" + resetUrl;

            sendEmail(user.getUserEmail(), subject, message);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        Users user = usersRepository.findByResetToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword)); // Ideally, hash the password
            user.setResetToken(null);
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
