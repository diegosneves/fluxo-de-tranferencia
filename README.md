# Fluxo de Transferência

[![wakatime](https://wakatime.com/badge/user/018bea20-dbbc-48e2-b101-5415903acf5a/project/018c0943-ec73-4e34-9362-f0ae72855dcf.svg)](https://wakatime.com/@diegosneves/projects/mtibjzqwtu)
[![CI Fluxo de Transferencia](https://github.com/diegosneves/fluxo-de-tranferencia/actions/workflows/ci.yml/badge.svg)](https://github.com/diegosneves/fluxo-de-tranferencia/actions/workflows/ci.yml)

---

## Swagger

- [Swagger - Local](http://localhost:8080/swagger-ui/index.html)
- [Api - Docs](http://localhost:8080/v3/api-docs)

---

### Instalação:

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

---
