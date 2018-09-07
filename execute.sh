#!/bin/sh

PIDFile="registrationApplication.pid"
function cleanup{
old_PID=$(<"$PIDFile")
if ps -p $old_PID > /dev/null
then
	echo "Killing the existing instance of application"
	kill -9 $old_pid
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

echo "Running the new instance of application"
mvn -U -Djasypt.encryptor.password=$JASYPT_SECRET -DskipTests=true spring-boot:run > log &
Application_PID=$!
echo $Application_PID > $PIDFile
echo "Done! Application is now up and running"

