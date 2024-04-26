FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless
ARG EXTRACTED=build/extracted
WORKDIR /application

COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
COPY ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]
