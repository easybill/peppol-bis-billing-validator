FROM openjdk:21

WORKDIR .

COPY ./build/peppol-bis-billing-validator--runner.jar .

ENTRYPOINT ["java", "-Xmx512m", "-jar", "peppol-bis-billing-validator--runner.jar"]