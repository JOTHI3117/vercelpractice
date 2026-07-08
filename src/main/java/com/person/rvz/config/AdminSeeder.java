package com.person.rvz.config;

import com.person.rvz.entity.Login;
import com.person.rvz.entity.Role;
import com.person.rvz.repository.LoginRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!loginRepository.existsByUsername("admin")) {
            Login admin = new Login();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            loginRepository.save(admin);
        }
    }
}
