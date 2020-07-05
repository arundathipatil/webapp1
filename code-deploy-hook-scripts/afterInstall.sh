echo "This is after install script"
cd /home/ubuntu/webapp
sudo chown -R ubuntu:ubuntu /home/ubuntu/*
sudo chmod +x BE-layer-1.0-SNAPSHOT.jar

#Kill application if already running
kill -9 $(ps -ef|grep BE-layer-1.0 | grep -v grep | awk '{print $2}')

source /etc/profile.d/envvariable.sh
nohup java -jar BE-layer-1.0-SNAPSHOT.jar > /home/ubuntu/webapp.log 2> /home/ubuntu/webapp.log < /home/ubuntu/webapp.log &
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/webapp/cloudwatch-agent.json -s