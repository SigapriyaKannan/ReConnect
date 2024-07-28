package com.dal.asdc.reconnect.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CorsProperties {
    
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    public String getAllowedOrigins() {
        return allowedOrigins;
    }
}
