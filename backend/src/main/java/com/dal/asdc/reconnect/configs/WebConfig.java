package com.dal.asdc.reconnect.configs;


import com.dal.asdc.reconnect.properties.CorsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) settings.
 * This class implements the WebMvcConfigurer interface to customize the CORS settings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CorsProperties corsProperties;

    /**
     * Configures the CORS settings for the application.
     *
     * @param registry the CorsRegistry to configure CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowedOrigins())
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
