FROM openjdk:17-alpine AS builder
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} findMyPet-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar findMyPet-0.0.1-SNAPSHOT.jar extract

FROM openjdk:17-alpine
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
COPY init/images/ ./files/
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "--spring.config.location=classpath:/application-docker.yml"]