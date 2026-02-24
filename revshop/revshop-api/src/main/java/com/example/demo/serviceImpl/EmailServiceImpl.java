package com.example.demo.serviceImpl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ajayreddygangasani16@gmail.com");  // ADD THIS
        message.setTo(to);
        message.setSubject("RevShop Password Reset OTP");
        message.setText(
                "Hello,\n\n" +
                "Your OTP for password reset is: " + otp +
                "\n\nThis OTP is valid for 5 minutes.\n\n" +
                "Regards,\nRevShop Team"
        );

        mailSender.send(message);
    }
}
