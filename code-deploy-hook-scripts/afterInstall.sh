echo "Running after install script"
cd /home/ubuntu/webapp
sudo chown -R ubuntu:ubuntu /home/ubuntu/*
sudo chmod +x BE-layer-1.0-SNAPSHOT.jar

#Kill application if already running
sudo kill -9 $(ps -ef|grep BE-layer-1.0 | grep -v grep | awk '{print $2}')