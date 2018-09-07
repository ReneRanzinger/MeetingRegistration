#!/bin/sh

PIDFile="registrationApplication.pid"

echo "Finding existing running application"
if [ ! -f $PIDFile ]
then
touch registrationApplication.pid
else
cleanup
fi

mvn -U -Djasypt.encryptor.password=$JASYPT_SECRET -DskipTests=true spring-boot:run > log &
Application_PID=$!
echo $Application_PID > $PIDFile

function cleanup(){
old_PID=$(<"$PIDFile")
if ps -p $old_PID > /dev/null
then
kill -9 $old_pid
fi
}

