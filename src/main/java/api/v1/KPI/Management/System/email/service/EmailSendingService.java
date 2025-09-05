package api.v1.KPI.Management.System.email.service;


import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.app.util.RandomUtil;
import api.v1.KPI.Management.System.email.enums.EmailType;
import api.v1.KPI.Management.System.exception.exps.AttemptLimitException;
import api.v1.KPI.Management.System.jwt.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailSendingService {
    @Value("${spring.mail.username}")
    private String fromAccaunt;
    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBoundleService boundleService;
    @Autowired
    private EmailHistoryService emailHistoryService;

    /// The sendRegistrationEmail(...) method sends an email to the user to complete the registration.
    /// The email is sent in HTML format and contains a confirmation button (Click there).
    /// The button redirects the user to an email verification link.
    /// The user ID is encrypted using JWT.
    /// Once the email is sent, a confirmation text is returned.
    public String sendRegistrationEmail(String profileId, String email, AppLanguage lang){
        String subject = "Complete Registration";
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        a {\n" +
                "            padding: 10px 30px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .buttin-link {\n" +
                "            text-decoration: none;\n" +
                "            color: white;\n" +
                "            background-color: indianred;\n" +
                "        }\n" +
                "        .buttin-link:hover {\n" +
                "            background-color: #dd4444;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Complete Registration</h1>\n" +
                "<p>\n" +
                "    %s\n" +
                "    <a class=\"buttin-link\"\n" +
                "\n" +
                "        href=\"%s/auth/registration/email-verification/%s/%s\" target=\"_blank\">Click there</a>\n" +
                "</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        body = String.format(body, boundleService.getMessage("registration.email.confirm.send", lang), "http://" + serverDomain + ":8080", JwtUtil.encodeVer(profileId), lang.name());
        sendMimeEmail(email, subject, body);
        return boundleService.getMessage("registration.email.confirm.send", lang);
    }

    /// The sendResetPasswordEmail(...) method sends an email to reset the user's password.
    /// A random verification code (code) is generated and sent via email.
    /// The content and subject of the email are translated to the user's language (lang).
    /// Once the email is sent, a message is returned indicating that it was sent successfully.
    public String sendResetPasswordEmail(String email, AppLanguage lang){
        String subject = boundleService.getMessage("send.reset.password.email.subject", lang);
        // generation code
        String code = RandomUtil.getRandomSmsCode();
        System.out.println(code);

        String body = boundleService.getMessage("send.reset.password.email.body", lang) + " " + code;
        // check
        checkAndSendMimeEmail(email, subject, body, code, lang);
        return boundleService.getMessage("resend.email.confirm.send", lang);
    }

    /// The sendUsernameChangeConfirmEmail(...) method sends a verification code to the user's email address to confirm the username (login) change.
    /// The code is sent along with the language-appropriate text and is sent via checkAndSendMimeEmail(...).
    public void sendUsernameChangeConfirmEmail(String email, AppLanguage lang) {
        String subject = boundleService.getMessage("send.username.change.confirm.email.subject",  lang);
        // generation code
        String code = RandomUtil.getRandomSmsCode();
        System.out.println(code);

        String body = boundleService.getMessage("send.username.change.confirm.email.body", lang) + " " + code;
        // check
        checkAndSendMimeEmail(email, subject, body, code, lang);
    }

    /// This method checks the number of messages sent to the user's email address before sending it. If the limit is not exceeded,
    /// it sends the message and stores it in the EmailHistory with type PASSWORD_RESET. If the limit is exceeded, it throws an AttemptLimitException error.
    public void checkAndSendMimeEmail(String email, String subject, String body, String code, AppLanguage lang){
        // check count
        Long count = emailHistoryService.getEmailCount(email);
        if (count >= 3){
            throw new AttemptLimitException(boundleService.getMessage("the.number.of.attempts.exceeded", lang));
        }
        // send
        sendMimeEmail(email, subject, body);
        // save
        emailHistoryService.save(email, code, EmailType.PASSWORD_RESET);
    }

    /// This method sends an HTML email message to the user asynchronously (CompletableFuture).
    /// If an error occurs, it throws a RuntimeException.
    public void sendMimeEmail(String email, String subject, String body){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(fromAccaunt);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            CompletableFuture.runAsync(() -> {
                mailSender.send(mimeMessage);
            });
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    /// This sendSimpleEmail method sends a simple text message to the user via email.
    /// It takes the recipient's email address, the subject, and the message text as parameters.
    public void sendSimpleEmail(String email, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAccaunt);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}