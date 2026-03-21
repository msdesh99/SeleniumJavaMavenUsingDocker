FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

RUN mkdir -p output/screenshots

COPY . /app

RUN mvn clean install -DskipTests

CMD ["mvn", "test"]