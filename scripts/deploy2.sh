#!/usr/bin/env bash


BUILD_JAR=$(ls /var/www/html/build/libs/*.jar)
JAR_NAME=cindy-server-0.0.1-SNAPSHOT.jar
echo ">>> build 파일명: $JAR_NAME" >> /var/www/html/deploy.log


echo ">>> build 파일 복사" >> /var/www/html/deploy.log
DEPLOY_PATH=/var/www/html/build/libs/
cp $BUILD_JAR $DEPLOY_PATH

echo ">>> 현재 실행중인 애플리케이션 Set 확인" >> /var/www/html/deploy.log
CURRENT_PROFILE=$(curl -s https://www.awesominki.shop/profile)
echo ">>> 현재 실행중인 프로필 $CURRENT_PROFILE" >> /var/www/html/deploy.log


if [ $CURRENT_PROFILE == dev ]
then
  IDLE_PROFILE=dev2
  IDLE_PORT=9001
elif [ $CURRENT_PROFILE == dev2 ]
then
  IDLE_PROFILE=dev
  IDLE_PORT=9000
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE" >> /var/www/html/deploy.log
  echo "> dev 할당합니다. IDLE_PROFILE: dev" >> /var/www/html/deploy.log
  IDLE_PROFILE=dev
  IDLE_PORT=9000
fi



echo "> $IDLE_PROFILE 에서 구동중인 애플리케이션 pid 확인" >> /var/www/html/deploy.log


DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME


echo "> $IDLE_PROFILE 배포" >> /var/www/html/deploy.log
nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $DEPLOY_JAR >> /var/www/html/deploy.log 2>/var/www/html/deploy_err.log &

# Nginx Port 스위칭을 위한 스크립트
echo "> 스위칭" >> /var/www/html/deploy.log
sleep 30


