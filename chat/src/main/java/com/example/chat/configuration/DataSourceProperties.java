package com.example.chat.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="spring.datasource")
public class DataSourceProperties {
    private String url;
    private String password;
    private String username;
}
