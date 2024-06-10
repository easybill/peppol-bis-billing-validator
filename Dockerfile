FROM openjdk:21-slim

WORKDIR .

COPY ./build/peppol-bis-billing-validator--runner.jar .

ENTRYPOINT ["java", "-jar", "peppol-bis-billing-validator--runner.jar"]