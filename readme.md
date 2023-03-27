# Boxinator-server

This repository contains a REST API for the Boxinator application. This project was made for the Noroff Accelerate case
period task. [Keycloak](https://www.keycloak.org/) is used for handling user credentials, and all other data is handled
by Postgres.

## API Endpoints

Most of the endpoints require a valid Authorization Bearer token to be included in the request headers. Refer to the
Swagger documentation for more in-depth usages for each of the endpoints.

Swagger documentation can be viewed locally
from: [https://localhost/swagger-ui/index.html#/](https://localhost/swagger-ui/index.html#/)

### /shipments

![image](https://user-images.githubusercontent.com/89595592/227528566-209f9731-b29f-4f21-b1af-dbbe84dfe233.png)
<br/>
Handles various different methods of getting/modifying shipments. For example, an admin user can look at all
ongoing shipments, and a basic user can get shipments associated with their own account.
<br/>

An admin is allowed to delete shipments and change the states of the shipments. For example an admin may change a
package from being <b>'CREATED'</b> to <b>'CANCELLED'</b>

Additionally, unregistered users may order shipments by using their email address. The email address gets sent a link
containing a temporary registration token which can be used to complete the registration process. Users can keep
ordering with using only an email address until the email gets used for a 'fully' registered account.

### /countries

![image](https://user-images.githubusercontent.com/89595592/227530222-a65b94e9-803c-40cb-8ce7-c1bf4ce73fd5.png)

Basic CRUD-operations for the valid countries in the application.

Note! Only an admin users can delete a country.

### /account

![image](https://user-images.githubusercontent.com/89595592/227535209-b71f83e3-4bfa-4fab-90ce-3aa2c92603e0.png)

Get/edit details related to an account. Admins are also allowed to delete accounts.

### /auth

![image](https://user-images.githubusercontent.com/89595592/227536400-f1784670-27bf-4763-9222-9acc5ef540de.png)

Endpoints for logging in, creating new accounts, and refreshing sessions.

Authorization tokens are valid for 300 seconds and refresh tokens are valid for 1800 seconds.

### /boxes

![image](https://user-images.githubusercontent.com/89595592/227537157-e31ea863-53c1-4241-85be-aa7038cdab1a.png)

All the different tiers of mysteryboxes a user can buy.

Currently there are four different boxes available. Basic, humble, deluxe and premium. Each have their own weight
respectively

## Running locally

### Only required to have an instance running:

The repository contains a docker-compose.yml which can be used to run the entire thing just by
running `docker-compose up` command. You only need to follow the step 3. from the Development section below to create
the required config file.

This will create and run the three containers: Postgres, Keycloak with the correct 'Boxinator' realm and the server.

When developing, the below step-by-step instructions should be followed to easily allow rebuilding/restarting the server
application separately from the keycloak and postgres instances.

### Development:

1. Setup keycloak with the instructions from [this](https://github.com/EaCase/keycloak-docker-compose) repository.
   Keycloak auto-imports the 'Boxinator' realm which is configured to work out-of-the box for local development with
   this API.
2. Setup a [Postgres](https://www.postgresql.org/) instance on your local machine.
3. Create an `application.properties` file into `src/main/resources/` folder, and copy the file below. Replace the
   postgres, SMTP(can be omitted, emails won't be sent in this case though) and client registration url(Can be also
   omitted) properties.
   Client registration url should point to a location in the client which can handle the registration via the
   registration token. Rest of the config does not need to be edited if step 1. has been followed.
4. Build & run
5. Access endpoints from: https://localhost/

```properties
# Postgres
spring.datasource.url={postgres_url}
spring.datasource.username={postgres_username}
spring.datasource.password={postgres_password}
# SMTP - for mail service - can be omitted
mail.auth.username={username}
mail.auth.password={password}
mail.smtp.sender={sender_email}
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.host=mail.smtp2go.com
mail.smtp.port=2525
# Client registration url for temporary accounts
client.url.registration={url}
# No need to edit properties below
# Dev cert
server.port=443
server.ssl.key-store=keystore.p12
server.ssl.key-store-password=tomcat
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat
# Auth client - Keycloak config
auth.url.login=http://localhost:8083/realms/Boxinator/protocol/openid-connect/token
auth.url.users=http://localhost:8083/admin/realms/Boxinator/users
auth.client=boxinator-client
auth.secret=Gsu5dxrNxUoX0LFdV9V90mENa51CGHiX
# Keycloak 'Boxinator' client id
auth.client.id=db095150-691c-4def-be44-2fdb448d5e8f
# Keycloak admin role id
auth.role.admin.id=bcf5b412-471c-4eb8-90e0-9fcead8fd5d3
# Keycloak user role id
auth.role.user.id=01ab668a-ee5f-49bd-b5ac-0b0ef3cbc1a2
# Jwt
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8083/realms/Boxinator
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/certs
jwt.auth.converter.resource-id=boxinator-client
jwt.auth.converter.principal-attribute=preferred_username
# Hibernate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true
# Swagger
springdoc.swagger-ui.operationsSorter=method
```

## MIT

Begin license text.

Copyright 2023

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

End license text.