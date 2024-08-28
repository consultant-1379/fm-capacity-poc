#! /bin/bash

source docker-functions.sh
 
exec docker-compose down -v
exec docker-compose kill


