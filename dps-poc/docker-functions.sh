#! /bin/bash

function dump() {
   eval echo "\$$1"    
}

function exec() {
  echo -n "Executing: $@ "
  if [ -z "$DOCKER_DEBUG" ];then
  eval $@ || exit 1 
  echo -e "\n" 
  fi
}

temp() {  
  docker exec -it $CONTAINER_ID bash
  docker cp $SRC_FILE $CONTAINER_ID:$DEST_FILE
  curl -L https://github.com/docker/machine/releases/download/v0.7.0/docker-machine-`uname -s`-`uname -m` > /usr/local/bin/docker-machine && chmod +x /usr/local/bin/docker-machine
}  

