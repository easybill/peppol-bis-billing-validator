FROM eclipse-temurin:21-alpine

WORKDIR .

COPY ./build/peppol-bis-billing-validator--runner.jar .

ENTRYPOINT ["java", "-jar", "peppol-bis-billing-validator--runner.jar"]