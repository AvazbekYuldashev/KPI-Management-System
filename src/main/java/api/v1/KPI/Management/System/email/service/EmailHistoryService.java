package api.v1.KPI.Management.System.email.service;


import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.email.entity.EmailHistoryEntity;
import api.v1.KPI.Management.System.email.enums.EmailType;
import api.v1.KPI.Management.System.email.repository.EmailHistoryRepository;
import api.v1.KPI.Management.System.exception.exps.AttemptLimitException;
import api.v1.KPI.Management.System.exception.exps.ResourceConflictException;
import api.v1.KPI.Management.System.exception.exps.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private ResourceBoundleService boundleService;

    /// This method stores the confirmation email (or password reset code) sent to the user in the database.
    /// It stores the email address, code, and email type (REGISTER, RESET_PASSWORD, etc.).
    public EmailHistoryEntity save(String email, String code, EmailType emailType) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setCode(code);
        entity.setEmailType(emailType);
        return emailHistoryRepository.save(entity);
    }

    /// This method counts the number of SMS messages sent to a user's phone number in the last 1 hour.
    /// For example, it is used to determine how many verification codes a user has received in 1 hour.
    public Long getEmailCount(String email){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusHours(1);
        return emailHistoryRepository.countByEmailAndCreatedDateBetween(email, from, now);
    }

    /// This method checks whether the email verification code sent by the user is valid and has not expired.
    /// It counts the number of incorrect attempts and, if necessary, blocks the user (after 3 incorrect attempts).
    public Boolean check(String email, String code, AppLanguage lang){
        // first last sms
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findTop1ByEmailOrderByCreatedDateDesc(email);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException(boundleService.getMessage("confirm.code.not.found", lang));
        }
        EmailHistoryEntity entity = optional.get();
        // check code
        if (!entity.getCode().equals(code)){
            emailHistoryRepository.updateAttemptCount(entity.getId());
            throw new ResourceConflictException(boundleService.getMessage("confirm.code.conflict", lang));
        }
        if (entity.getAttemptCount() >= 3){
            throw new AttemptLimitException(boundleService.getMessage("the.number.of.attempts.exceeded", lang));
        }
        // check time
        if (LocalDateTime.now().isAfter(entity.getCreatedDate().plusMinutes(15))){
            throw new ResourceConflictException(boundleService.getMessage("confirm.code.expired", lang));
        }
        return true;
    }


}
