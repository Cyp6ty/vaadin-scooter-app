package fi.sauli.service;

import com.nimbusds.jose.proc.SecurityContext;
import fi.sauli.user.AppUser;
import fi.sauli.user.AppUserRepository;
import fi.sauli.user.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // --- Konstruktor ---
    public UserService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Luo uuden käyttäjän ja tallentaa sen tietokantaan
    public void registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Käyttäjätunnus on jo käytössä");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
    }

    // Hakee tällä hetkellä kirjautuneen käyttäjän
    public AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Kirjautunutta käyttäjää ei löytynyt"));
    }

    // Tallentaa käyttäjän profiilikuvan polun
    public void saveProfileImagePath(String imagePath) {
        AppUser user = getCurrentUser();
        user.setProfileImagePath(imagePath);
        userRepository.save(user);
    }
}
