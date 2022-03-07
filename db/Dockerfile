FROM eclipse-temurin:17-jre
COPY target/db.jar discordbotDb.jar
ENTRYPOINT ["java","-Xmx32m -Xss256k -jar","/discordbotDb.jar"]