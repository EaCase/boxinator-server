FROM quay.io/keycloak/keycloak:21.0

COPY ./data/import /opt/keycloak/data/import

RUN /opt/keycloak/bin/kc.sh import --file /opt/keycloak/data/import/boxinator.json

CMD ["start-dev"]