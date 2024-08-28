mkdir /tmp/migration
mkdir /tmp/migration/exported/
mkdir /tmp/migration/converted

java -jar  -Dlog4j.debug -Dlog4j.configuration=file:/tmp/zetto.xml es-migration-1.0.0-SNAPSHOT.jar solr:8983 10.247.246.8:9200 1 4 4 DAILY_INDICES 20000
java -jar -Xms4g -Xmx4g  -Dlog4j.debug -Dlog4j.configuration=file:/tmp/zetto.xml es-migration-1.0.0-SNAPSHOT.jar solr:8983 10.247.246.8:9200 5 4 4 DAILY_INDICES 20000


yum install jq

run solr.sh solr 1

java -jar ./target/es-migration-1.0.0-SNAPSHOT.jar localhost:39981 0.0.0.0:9200 1 1 1 DAILY_INDICES 2

docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.8.12
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xms8g -Xmx8g" docker.elastic.co/elasticsearch/elasticsearch:6.8.4

docker run --rm -it -v /tmp/migration/logstash/:/usr/share/logstash/config/ docker.elastic.co/logstash/logstash:6.8.12

docker ps
docker ps -A
curl -X GET "localhost:9200/_cat/nodes?v&pretty"

curl localhost:9200/_cat/indices?v
curl 10.247.246.8:9200/_cat/allocation?v
curl 10.247.246.8:9200/_cat/indices?v
curl 10.247.246.8:9200/_cat/count/fm*?v

curl 'localhost:9200/edelpao/_settings?include_defaults=true&flat_settings=true&human&pretty'

curl 'localhost:9200/edelpao/_settings?include_defaults=true&flat_settings=true&human&pretty' | grep translog

curl --location --request PUT 'http://localhost:9200/fm*/_settings' \
 --header 'Content-Type: application/json' \
 --data-raw '{
    "index.refresh_interval" : 1s
}'

POST /twitter/_forcemerge?max_num_segments=5

curl localhost:9200/<indexname>/_search?pretty

curl localhost:9200/<indexname>/_search?pretty=true&p=*.*

curl "localhost:9200/fm_history_20200902/_search?pretty=true&q=*.*&size=1"

curl -XDELETE http://10.247.246.8:8900/fm*

// OPTION 2 for elastich + logstash + kibana
https://elk-docker.readthedocs.io/
sysctl -w vm.max_map_count=262144


// OPTION 3 dockercompose
https://medium.com/@harisshafiq08/elk-stack-deployment-through-docker-compose-98ce40ff2fb6

//// ------------------------------------------------------------------------------------------ ////
To keep a container running when you start it with docker-compose, use the following command

command: tail -F anything

So your docker-compose.yml becomes

version: '2'
services:
  my-test:
    image: ubuntu
    command: tail -F anything
and you can run a shell to get into the container using the following command

docker exec -i -t logstash bash

bin/logstash-plugin update logstash-input-file (serve la versione 4.2.1 per il read-mode)
https://github.com/logstash-plugins/logstash-input-file/blob/v4.2.1/CHANGELOG.md

## Num of replicas 0
curl -XPUT "localhost:9200/_template/logstash_template" -H 'Content-Type: application/json' -d'
{
  "index_patterns": ["logstash*"],
  "settings": {
    "number_of_replicas": 0
  }
}





