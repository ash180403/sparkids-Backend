package com.Cascade.Form;

import com.Cascade.Form.Repository.ContactRepository;
import com.Cascade.Form.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FormApplication  {

    @Autowired
    private ContactRepository contactRepository;

    public static void main(String[] args) {
        SpringApplication.run(FormApplication.class, args);

    }

    @Configuration
public static class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") 
                .allowedOrigins("https://cascade-technologies-solutions.github.io/Sparkids-deployed/") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowCredentials(true);
    }
}
}
