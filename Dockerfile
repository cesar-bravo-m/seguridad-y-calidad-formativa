FROM mysql:8.0 as mysql
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=formativa
EXPOSE 3306

FROM eclipse-temurin:17-jdk as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=mysql /var/lib/mysql /var/lib/mysql
COPY --from=mysql /etc/mysql /etc/mysql
COPY --from=mysql /usr/bin/mysqld /usr/bin/mysqld
COPY --from=mysql /usr/sbin/mysqld /usr/sbin/mysqld

# Create startup script
RUN echo '#!/bin/bash\n\
/usr/sbin/mysqld --daemonize\n\
java -cp app:app/lib/* com.example.formativa.FormativaApplication\n\
' > /start.sh && chmod +x /start.sh

EXPOSE 8080 3306
ENTRYPOINT ["/start.sh"]
