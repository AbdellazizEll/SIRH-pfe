package com.example.anywrpfe.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(20)); // Set the maximum file size
        factory.setMaxRequestSize(DataSize.ofMegabytes(20)); // Set the maximum request size
        return factory.createMultipartConfig();
    }

    @Bean
    public FileUploadProperties fileUploadProperties() {
        return new FileUploadProperties();
    }
}
