# Case TOTVS

### Para rodar a aplicação é necessário primeiro iniciar um container com o Postgres no folder **totvs-api**:

```
docker-compose -f ./src/main/docker/postgresql.yml up
```

***Observação importante***: a porta está mapeada para a 5432, caso exista outra aplicação rodando nessa porta o container não iniciará.

### Para rodar o totvs-api é necessário em back-case executar o comando:

```
./mvnw (ou só mvnw caso seja Windows)
```

### Para rodar o front-end basta executar os comandos:
```
npm install
npm start
```

### Para o login na aplicação:
-   Usuário padrão:
```
    Login: user@tovs.com
    Password: user1234
```

- Usuário administrativo:
```
  Login: admin@totvs.com
  Password: admin1234
```


### Teste

É necessário estar no totvs-api folder

```
mvn test
```

### JavaDoc:

Disponivel no folder totvs-api/javaDoc. Abrir o arquivo index.html.



