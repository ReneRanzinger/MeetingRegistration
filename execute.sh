#!/bin/bash

PIDFile="registrationApplication.pid"
cleanup() {
old_PID=$(cat $PIDFile)
if ps -p $old_PID > /dev/null;
then
echo "Killing the existing instance of application: $old_PID" && kill -9 $old_PID;
fi
}
echo "Finding existing running application."
if [ ! -f $PIDFile ]
then
echo "No running instance of application found. Creating runningApplication.pid file to store the PID of the next process."
touch registrationApplication.pid
else
cleanup
fi
echo "Setting environment variables if any"
if [ -f ~/.secrets ]; then
    . ~/.secrets
fi
echo "Running the new instance of application"
mvn -U -Djasypt.encryptor.password=$JASYPT_SECRET -DskipTests=true spring-boot:run > log &
Application_PID=$!
echo $Application_PID > $PIDFile
echo "Done! Application is now up and running"

