package diegosneves.github.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getInfo())
                .tags(getTags());
    }

    private Info getInfo() {
        return new Info()
                .version("0.0.1")
                .title("Fluxo de Transferencia")
                .description("APIs do Fluxo de Transferencia")
                .contact(new Contact().email("neves.diegoalex@outlook.com").url("https://github.com/diegosneves/fluxo-de-tranferencia"));
    }

    private List<Tag> getTags() {
        return List.of();
    }

}
