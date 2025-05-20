#!/bin/bash

BUILD_JAR=$(ls /home/ubuntu/sb09-deploy/build/libs/*SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "## build file name : $JAR_NAME" >> /home/ubuntu/sb09-deploy/spring-deploy.log

echo "## copy build file" >> /home/ubuntu/sb09-deploy/spring-deploy.log
DEPLOY_PATH=/home/ubuntu/sb09-deploy/
cp $BUILD_JAR $DEPLOY_PATH 

echo "## current pid" >> /home/ubuntu/sb09-deploy/spring-deploy.log
CURRENT_PID=$(ps -ef | grep "java" | awk 'NR==1 {print $2}')

if [ -z $CURRENT_PID ]
then
  echo "## 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/sb09-deploy/spring-deploy.log
else
  echo "## kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID 
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "## deploy JAR file"   >> /home/ubuntu/sb09-deploy/spring-deploy.log
nohup /home/ubuntu/jdk21/bin/java -jar $DEPLOY_JAR >> /home/ubuntu/sb09-deploy/spring-deploy.log 2> /home/ubuntu/sb09-deploy/spring-deploy_err.log &