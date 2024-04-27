#FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless
#ARG EXTRACTED=build/extracted
#WORKDIR /application
#
#COPY ${EXTRACTED}/dependencies/ ./
#COPY ${EXTRACTED}/spring-boot-loader/ ./
#COPY ${EXTRACTED}/snapshot-dependencies/ ./
#COPY ${EXTRACTED}/application/ ./
#ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]

FROM  public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023 as builder

# TODO - Should be build/libs/application.jar in Builder?

ARG JAR_FILE=build/libs/application.jar
WORKDIR /application
ARG EXTRACTED=/application/extracted

COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract --destination ${EXTRACTED}


FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless

WORKDIR /application


ARG EXTRACTED=/application/extracted
COPY --from=builder ${EXTRACTED}/dependencies/ ./
COPY --from=builder ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=builder ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=builder ${EXTRACTED}/application/ ./
EXPOSE 8080

RUN dnf install -y shadow-utils && \
    groupadd -r spring && \
    useradd -r -g spring -d /application -s /sbin/nologin spring && \
    chown -R spring:spring /application


USER spring
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]