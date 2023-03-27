# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the Gradle build files and the source code to the container
COPY build.gradle settings.gradle ./
COPY src ./src
COPY keystore.p12 ./

# Download and install Gradle
ENV GRADLE_VERSION 7.6.1
RUN apk update && apk add unzip
RUN apk add --no-cache curl
RUN curl -L https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip -o gradle-$GRADLE_VERSION-bin.zip && \
    unzip gradle-$GRADLE_VERSION-bin.zip && \
    mv gradle-$GRADLE_VERSION /opt/gradle && \
    rm gradle-$GRADLE_VERSION-bin.zip

ENV GRADLE_HOME /opt/gradle
ENV PATH $PATH:$GRADLE_HOME/bin

# Build the application with Gradle without tests since Postgres instance wont be available
RUN gradle build -x test

# Expose port 8080
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "build/libs/boxinator.jar"]