package com.Cascade.Form.Congif;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCongif {

    @Bean
    public OpenAPI myCustomConfig() {
    	return new OpenAPI()
                .info(new Info()
                        .title("Sparkids APIs")
                        .version("1.0.0")  
                        .description("API documentation for Sparkids platform")
                        .contact(new Contact()
                                .name("Sparkids Team")
                                .email("support@sparkids.com")
                                .url("https://www.sparkids.com.com")) 
                );
    }
}
