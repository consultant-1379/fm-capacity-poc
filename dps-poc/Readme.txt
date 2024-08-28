docker run -it --entrypoint "bash" armdocker.rnd.ericsson.se/proj_oss_releases/enm/jboss-dps
docker run -it --entrypoint "bash" --volumes-from 930f150ad6d7 armdocker.rnd.ericsson.se/proj_oss_releases/enm/jboss-dps
docker cp 'container':path/to/file dest/path


Connect to JMS remote process:
service:jmx:remoting-jmx://localhost:9999

Credentials: root/shroot


[versant@b94a828de339 bin]$ /ericsson/versant/bin/db2tty -D dps_integration -i | grep OpenAlarm | grep Total

