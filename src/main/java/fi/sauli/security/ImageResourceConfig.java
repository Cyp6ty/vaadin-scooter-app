package fi.sauli.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class ImageResourceConfig implements WebMvcConfigurer {

    // Määrittää polun, josta ladatut kuvat haetaan
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Path.of("uploaded-profile-images").toFile().getAbsolutePath();

        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}