echo "Running after install script"
cd /home/ubuntu/webapp
sudo chown -R ubuntu:ubuntu /home/ubuntu/*
sudo chmod +x BE-layer-1.0-SNAPSHOT.jar

#Kill application if already running
kill -9 $(lsof -t -i:8080)