FROM openjdk:17-alpine
COPY target/bot-*dependencies.jar discordbot.jar
ENTRYPOINT ["java","-jar","/discordbot.jar"]