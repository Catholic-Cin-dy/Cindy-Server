#!/usr/bin/env bash


echo "> 현재 구동중인  Port 확인" >> /var/www/html/deploy.log
CURRENT_PROFILE=$(curl -s https://www.awesominki.shop/profile)

if [ $CURRENT_PROFILE == dev ]
then
  IDLE_PORT=9001
elif [ $CURRENT_PROFILE == dev2 ]
then
  IDLE_PORT=9000
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE" >> /var/www/html/deploy.log
  echo "> 9000을 할당합니다." >> /var/www/html/deploy.log
  IDLE_PORT=9000
fi

echo "> 전환할 Port: $IDLE_PORT" >> /var/www/html/deploy.log
echo "> Port 전환" >> /var/www/html/deploy.log
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

PROXY_PORT=$(curl -s https://www.awesominki.shop/profile)
echo "> Nginx Current Proxy Port: $PROXY_PORT" >> /var/www/html/deploy.log

echo "> Nginx Reload" >> /var/www/html/deploy.log
sudo service nginx reload # reload는 설정만 재적용하기 때문에 바로 적용이 가능합니다.


if [ $CURRENT_PROFILE == dev ]
then
  fuser -k 9000/tcp
  echo ">>> kill 9000 port" >> /var/www/html/deploy.log
  sleep 5
else
  fuser -k 9001/tcp
  echo ">>> kill 9001 port" >> /var/www/html/deploy.log
  sleep 5
fi

echo "> 스위칭 실행완료" >> /var/www/html/deploy.log


