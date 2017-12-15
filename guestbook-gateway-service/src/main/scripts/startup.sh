#!/bin/ash

if [ -e /config/dictionaries/env.sh ]
then
  echo "Sourcing /config/dictionaries/env.sh"
  source /config/dictionaries/env.sh
else
  echo "/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\"
  echo "No /data/env.sh found, you've been warned!"
  echo "/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\"
fi

if /usr/local/bin/confd -onetime -backend env
  then
    echo
    echo "w00t w00t!! config has been generated, let's start our sprinbgoot app now"
    /deployments/run-java.sh
 else
    echo "You failed at starting confd and/or springboot properly"
fi
