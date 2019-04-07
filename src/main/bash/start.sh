#!/bin/bash
if [[ -f /config/application.properties ]]; then
   cp /config/application.properties /application.properties
fi
java -jar /app.jar