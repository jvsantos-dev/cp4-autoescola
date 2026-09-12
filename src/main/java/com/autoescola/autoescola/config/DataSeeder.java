package com.autoescola.autoescola.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.autoescola.autoescola.model.Perfil;
import com.autoescola.autoescola.model.Usuario;
import com.autoescola.autoescola.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String EMAIL_ADMIN_PADRAO = "admin@autoescola.com";
    private static final String SENHA_ADMIN_PADRAO = "Admin@123";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .email(EMAIL_ADMIN_PADRAO)
                    .senha(passwordEncoder.encode(SENHA_ADMIN_PADRAO))
                    .perfil(Perfil.ADMIN)
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuário administrador padrão criado: {} / senha: {}", EMAIL_ADMIN_PADRAO, SENHA_ADMIN_PADRAO);
        }
    }
}
