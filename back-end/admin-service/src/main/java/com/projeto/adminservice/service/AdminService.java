package com.projeto.adminservice.service;

import com.projeto.adminservice.entity.Admin;
import com.projeto.adminservice.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Admin createAdmin(String username, String email, String rawPassword) {
        String passwordHash = passwordEncoder.encode(rawPassword);
        Admin admin = Admin.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordHash)
                .build();
        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin updateAdmin(Long id, String username, String email) {
        Admin admin = adminRepository.findById(id).orElseThrow();
        admin.setUsername(username);
        admin.setEmail(email);
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public boolean checkPassword(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}
