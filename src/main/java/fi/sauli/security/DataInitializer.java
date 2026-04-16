package fi.sauli.security;

import fi.sauli.user.AppUser;
import fi.sauli.user.AppUserRepository;
import fi.sauli.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    // Alustaa testikäyttäjät (Admin, Super, User)
    @Bean
    CommandLineRunner initUsers(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!appUserRepository.existsByUsername("Admin")) {
                AppUser admin = new AppUser();
                admin.setUsername("Admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(Role.ADMIN));
                appUserRepository.save(admin);
            }

            if (!appUserRepository.existsByUsername("Super")) {
                AppUser superUser = new AppUser();
                superUser.setUsername("Super");
                superUser.setPasswordHash(passwordEncoder.encode("super123"));
                superUser.setRoles(Set.of(Role.SUPER));
                appUserRepository.save(superUser);
            }

            if (!appUserRepository.existsByUsername("User")) {
                AppUser user = new AppUser();
                user.setUsername("User");
                user.setPasswordHash(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(Role.USER));
                appUserRepository.save(user);
            }
        };
    }
}
