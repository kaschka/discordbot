FROM openjdk:17-alpine
COPY target/db.jar discordbotDb.jar
ENTRYPOINT ["java","-Xmx32m -Xss256k -jar","/discordbotDb.jar"]