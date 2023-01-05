FROM java:8
WORKDIR /
ADD app-all.jar Profit\app\build\libs
EXPOSE 8080
CMD java - jar app-all.jar