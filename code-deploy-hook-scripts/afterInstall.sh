echo "Running after install script"
cd /home/ubuntu/webapp
sudo chown -R ubuntu:ubuntu /home/ubuntu/*
sudo chmod +x demo-0.0.1-SNAPSHOT.jar

#Kill application if already running
kill -9 $(ps -ef|grep demo-0.0.1 | grep -v grep | awk '{print $2}')