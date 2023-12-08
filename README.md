# Fluxo de Transferência [![wakatime](https://wakatime.com/badge/user/018bea20-dbbc-48e2-b101-5415903acf5a/project/018c0943-ec73-4e34-9362-f0ae72855dcf.svg)](https://wakatime.com/@diegosneves/projects/mtibjzqwtu) [![CI Fluxo de Transferencia](https://github.com/diegosneves/fluxo-de-tranferencia/actions/workflows/ci.yml/badge.svg)](https://github.com/diegosneves/fluxo-de-tranferencia/actions/workflows/ci.yml)

Fluxo de Transferência

```yaml
version: '3.9'

services:
  sqldb:
    image: mysql:latest
    restart: always
    container_name: sql_transferencia_db
    networks:
      - fluxo-bridge
    ports:
      - "3307:3306"
    volumes:
      - fluxo-db:/var/lib/mysql
    environment:
      - MYSQL_ROOT_PASSWORD=Fluxo@123
      - MYSQL_DATABASE=transferenciadb

  fluxo-app:
    image: diegoneves/fluxo-tranferencia:latest
    container_name: fluxo_transferencia
    ports:
      - "8080:8080"
    depends_on:
      - sqldb
    networks:
      - fluxo-bridge
    environment:
      - DB_HOST=sql_transferencia_db
      - DB_PORT=3306

volumes:
  fluxo-db:

networks:
  fluxo-bridge:
    driver: bridge
```

Lembre-se de estar no diretório onde o seu arquivo `docker-compose.yaml` está localizado antes de executar esses comandos.

Para executar esse arquivo docker-compose.yaml, use o comando:
```shell
docker-compose up -d
```

Se quiser que o Docker Compose reconstrua as imagens antes de iniciar os contêineres, você pode adicionar a opção --build:
```shell
docker-compose up --build -d
```

Para parar o `docker-compose.yaml` execute o comando abaixo:
```shell
docker-compose down
```

http://localhost:8080/

---

## Docker:

- _Para visualizar os logs a cada segundo, pode usar o seguinte comando:_
```shell
watch -n1 docker logs fluxo_transferencia
```


---
>_**Importante** lembrar que a rede(`--network`) deve ser a **mesma** da base de dados_

---
## [Swagger](https://springdoc.org/#Introduction)

- [Swagger - Local](http://localhost:8080/swagger-ui/index.html)
- [Api - Docs](http://localhost:8080/v3/api-docs)

### Pom.xml:

- dependencia:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

- plugin:
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>3.2.0</version>
</plugin>

<plugin>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-maven-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <execution>
            <id>integration-test</id>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

- classe de configuracao:

```java
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
```

- `application.yaml`:

```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

---

## Java:

### Cors:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // Permitir solicitações de qualquer origem
        config.addAllowedHeader("*"); // Permitir qualquer cabeçalho
        config.addAllowedMethod("*"); // Permitir qualquer método (GET, POST, etc.)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
```

### WebSecurityConfiguration:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
  	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  		http
  		 	.authorizeHttpRequests((authorizeHttpRequests) ->
  		 		authorizeHttpRequests.anyRequest().permitAll()
  		 	);
  		return http.build();
  	}

}
```

---

