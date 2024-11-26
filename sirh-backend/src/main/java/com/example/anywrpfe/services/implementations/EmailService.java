package com.example.anywrpfe.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service("implEmailService")
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String fullName, String activationUrl, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            log.info("Preparing to send email to {}", to);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(buildActivationEmailContent(fullName, activationUrl), true);  // true indicates HTML content
            mailSender.send(message);
            log.info("Email successfully sent to {}", to);

        } catch (MailException e) {
            log.error("Error sending email: SMTP connection error", e);
            throw e;
        } catch (MessagingException e) {
            log.error("Error creating email message", e);
            throw e;
        }
    }

    private String buildActivationEmailContent(String name, String activationLink) {
        return "<p>Hello, " + name + "</p>"
                + "<p>Thank you for registering. Please click the following link to activate your account:</p>"
                + "<p><a href=\"" + activationLink + "\">Activate Now</a></p>"
                + "<p>If you did not request this, please ignore this email.</p>";
    }

    // New method for sending evaluation result notifications
    public void sendEvaluationResultEmail(String to, String fullName, String trainingTitle, boolean isApproved, String rejectionReason) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            log.info("Preparing to send evaluation result email to {}", to);
            helper.setTo(to);
            helper.setSubject("Training Evaluation Result: " + trainingTitle);
            helper.setText(buildEvaluationResultContent(fullName, trainingTitle, isApproved, rejectionReason), true);
            mailSender.send(message);
            log.info("Evaluation result email successfully sent to {}", to);

        } catch (MailException e) {
            log.error("Error sending evaluation result email: SMTP connection error", e);
            throw e;
        } catch (MessagingException e) {
            log.error("Error creating evaluation result email message", e);
            throw e;
        }
    }

    private String buildEvaluationResultContent(String name, String trainingTitle, boolean isApproved, String rejectionReason) {
        if (isApproved) {
            return "<p>Dear " + name + ",</p>"
                    + "<p>Congratulations! Your training titled <strong>" + trainingTitle + "</strong> has been successfully evaluated and approved.</p>"
                    + "<p>You have now completed this training. Keep up the great work!</p>";
        } else {
            return "<p>Dear " + name + ",</p>"
                    + "<p>We regret to inform you that your training titled <strong>" + trainingTitle + "</strong> has not been approved upon evaluation.</p>"
                    + "<p>Reason: " + rejectionReason + "</p>"
                    + "<p>You may review the training requirements and consider reattempting. For further assistance, please reach out to your manager.</p>";
        }
    }

    public void sendPasswordChangeConfirmationEmail(String to, String fullName) throws MessagingException {
        String subject = "Password Change Confirmation";
        String activationLink = ""; // Not needed for password change
        sendEmail(to, fullName, null, subject, buildPasswordChangeEmailContent(fullName));
    }

    private String buildPasswordChangeEmailContent(String name) {
        return "<p>Dear " + name + ",</p>"
                + "<p>Your password has been successfully changed.</p>"
                + "<p>If you did not perform this action, please contact support immediately.</p>";
    }


    public void sendEmail(String to, String fullName, String activationUrl, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            log.info("Preparing to send email to {}", to);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML content
            mailSender.send(message);
            log.info("Email successfully sent to {}", to);

        } catch (MailException e) {
            log.error("Error sending email: SMTP connection error", e);
            throw e;
        } catch (MessagingException e) {
            log.error("Error creating email message", e);
            throw e;
        }
    }



}
