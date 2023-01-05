FROM openjdk:19
WORKDIR /
ADD * Profit
EXPOSE 8080
CMD ["java", "-jar", "Profit/app/build/libs/app.jar"]