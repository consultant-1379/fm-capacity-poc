#! /bin/bash
#
# 
# 
source docker-env-functions.sh

set -e 

install_rpms_from_nexus
install_rpms_from_iso
cleanup_deployment
copy_jboss_config
 
wait_dps_integration
