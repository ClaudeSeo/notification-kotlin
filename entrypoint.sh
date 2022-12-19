#!/bin/sh

MEMORY_SIZE=2

java -server \
  -Xms${MEMORY_SIZE}g \
  -Xmx${MEMORY_SIZE}g \
  -XX:FlightRecorderOptions=stackdepth=512 \
  -Dspring.profiles.active=$ENV \
  -jar ./notification-api.jar
