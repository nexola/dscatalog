package com.nexola.dscatalog.services;

import com.nexola.dscatalog.dto.EmailDTO;
import com.nexola.dscatalog.entities.PasswordRecover;
import com.nexola.dscatalog.entities.User;
import com.nexola.dscatalog.repositories.PasswordRecoverRepository;
import com.nexola.dscatalog.repositories.UserRepository;
import com.nexola.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverURI;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    public void createRecoverToken(EmailDTO body) {
        User user = repository.findByEmail(body.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Email não encontrado");
        }

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(UUID.randomUUID().toString());
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));

        entity = passwordRecoverRepository.save(entity);

        String bodyEmail = "Acesse o link para definir uma nova senha\n\n"
                + recoverURI + entity.getToken() + "\n\nVálido por " + tokenMinutes + " minutos";

        emailService.sendEmail(entity.getEmail(), "Recuperação de senha", bodyEmail);
    }
}
