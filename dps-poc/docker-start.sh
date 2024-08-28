#! /bin/bash

source docker-functions.sh

exec dump COMPOSE_PROJECT_NAME
exec dump JBOSS_IMAGE_START_CMD
exec dump DOCKER_MACHINE_HOST_IP
exec export COMPOSE_HTTP_TIMEOUT=500 
exec dump COMPOSE_HTTP_TIMEOUT

exec docker-compose kill
#[ "$1" != "--no-pull" ] && exec docker-compose pull --ignore-pull-failures
[ "$1" != "--no-pull" ] && exec docker-compose pull
exec docker-compose build --pull
exec docker-compose up --force-recreate -d
exec docker-compose logs

