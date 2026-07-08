package com.person.rvz.serviceimpl;

import com.person.rvz.entity.Login;
import com.person.rvz.entity.Role;
import com.person.rvz.repository.LoginRepository;
import com.person.rvz.security.JwtUtil;
import com.person.rvz.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public String login(String username, String rawPassword) {
        Login login = loginRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(rawPassword, login.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return jwtUtil.generateToken(login.getUsername(), login.getRole().name());
    }

    @Override
    public void updatePassword(String requesterUsername, String targetUsername, String newPassword) {
        Login requester = loginRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid requester"));

        if (requester.getRole() != Role.ADMIN) {
            throw new SecurityException("Only admins can update other users' passwords");
        }

        Login target = loginRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + targetUsername));

        target.setPassword(passwordEncoder.encode(newPassword));
        loginRepository.save(target);
    }
}
