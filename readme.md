# Boxinator-server

This repository contains a REST API for the Boxinator application. This project was made for the Noroff Accelerate case period task.

## Overview

#### Accounts

There are three types of accounts. 'Admin' - All actions available, 'Basic' - Limited functionality, 'Guest' - Allowed
to create shipments and upgrade the account to 'Basic' via a link provided in the email.

### Running locally

1. Setup keycloak with the instructions from [this](https://github.com/EaCase/keycloak-docker-compose) repository.
2. Setup Postgres instance.
3. Create an `application.properties` file into `src/main/resources/` folder, and copy the file below. Replace the
   postgres, SMTP and client registration url properties. No need to touch other config when using keycloak from step 1.

```properties
# Postgres
spring.datasource.url={postgres_url}
spring.datasource.username={postgres_username}
spring.datasource.password={postgres_password}
# SMTP - If using smtp2go, need to only replace the top 3
mail.auth.username={username}
mail.auth.password={password}
mail.smtp.sender={sender_email}
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.host=mail.smtp2go.com
mail.smtp.port=2525
# Client registration url for temporary accounts
client.url.registration={url}
# Auth client - Keycloak config - If following Step 1. all this will be correct
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
# Jwt - If following Step 1. all this will be correct
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

### API Endpoints

Our API consists of multiple different endpoints aimed at completeing different tasks.

#### Shipment controller 
![image](https://user-images.githubusercontent.com/89595592/227528566-209f9731-b29f-4f21-b1af-dbbe84dfe233.png)
<br/>
Our shipment controller handles various different methods of getting shipments. For example and admin user can get all shipments, but a basic user can only get shipments associated with their own account.
<br/>

An admin is allowed to delete shipments and change the states of the shipments. For example an admin may change a package from being <b>'CREATED'</b> to <b>'CANCELLED'</b>

Additionally an unregistered user may order a shipment and after that will recieve an email asking them to sign-up to be able to see all their shipments made from there on forwards

#### Country controller
![image](https://user-images.githubusercontent.com/89595592/227530222-a65b94e9-803c-40cb-8ce7-c1bf4ce73fd5.png)

Our country controller contains different endpoints to allow for the creation of countries, editing them and even deleting a country.

Note! Only an admin users can delete a country. 

#### Account controller 
![image](https://user-images.githubusercontent.com/89595592/227535209-b71f83e3-4bfa-4fab-90ce-3aa2c92603e0.png)

Our account controller has the ability to get an accounts details once a user has logged in. For example this functionality is used on the front end to show account details. A user is allowed to make changes to their own account details.

In our current set up only an admin is allowed to delete accounts.

#### Auth controller 
![image](https://user-images.githubusercontent.com/89595592/227536400-f1784670-27bf-4763-9222-9acc5ef540de.png)

Our auth controller handles all requests related to making an account and logging in. It also handles sending a refresh token to the user that is logged in.

Authorization tokens are valid for 300 seconds and refresh tokens are valid for 1800 seconds.

#### Box controller
![image](https://user-images.githubusercontent.com/89595592/227537157-e31ea863-53c1-4241-85be-aa7038cdab1a.png)

Our box controller handles all the different tiers of mysteryboxes a user can buy.

Currently there are four different boxes available. Basic, humble, deluxe and premium. Each have their own weight respectively 

