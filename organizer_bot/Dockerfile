FROM openjdk:14
COPY ./targer/organizer_bot-1.0.jar /home/pi/organizer/organizer_bot.jar
WORKDIR /home/pi/organizer
RUN java -jar organizer_bot.jar
CMD ["java", "-jar","organizer_bot.jar"]