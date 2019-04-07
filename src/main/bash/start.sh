#!/bin/bash
if [[ -f /config/application.properties ]]; then
   cp /config/application.properties /application.properties
fi

# In real world here we may want to detach java from bash, but for simplicity reasons let's keep it simple
java -jar /app.jar