package com.school.campusexpress.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("校园快递系统 API 文档")
                        .version("1.0")
                        .description("校园快递/商城系统接口文档")
                        .contact(new Contact()
                                .name("项目组")
                                .email("example@email.com")));
    }
}
