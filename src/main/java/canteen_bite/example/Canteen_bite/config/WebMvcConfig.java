package canteen_bite.example.Canteen_bite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontendurl;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = new ArrayList<>();
        if (frontendurl != null && !frontendurl.isBlank()) {
            for (String origin : frontendurl.split(",")) {
                String trimmed = origin.trim();
                if (!trimmed.isEmpty()) {
                    origins.add(trimmed);
                }
            }
        }
        if (origins.isEmpty()) {
            origins.add("https://smart-canteen-bite.netlify.app/");
            origins.add("https://smart-canteen-bite.netlify.app/");
        }

        registry.addMapping("/**") // Allow CORS on all endpoints
                .allowedOrigins(origins.toArray(new String[0])) // React app origin(s)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                .allowedHeaders("*") // Allow all headers (including Authorization)
                .allowCredentials(true); // Allow cookies/authorization headers
    }
}
