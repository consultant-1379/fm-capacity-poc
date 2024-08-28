docker run -it -v .:/tmp/solr -p 8983:8983 solr:5-slim

docker exec -i -t <container> bash

mkdir /opt/solr/server/solr/collection1/conf
copy from /tmp solrconfig.xml and schema.xml in /opt/solr/server/solr/collection1/conf

create collection1 from GUI
http://localhost:8983/solr/

# solr create -c collection1

# IMPORT from json file
curl 'http://localhost:8983/solr/collection1/update?commit=true' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-06.json -H 'Content-type:application/json'
# DELETE All data
curl "http://localhost:8983/solr/collection1/update?stream.body=<delete><query>*:*</query></delete>&commit=true"
# READ all DATA
curl http://localhost:8983/solr/collection1/select?q=*:*
curl http://localhost:8983/solr/collection1/select?q=*:*&stats=true&stats.field=insertTime&rows=0&indent=true

curl 'http://localhost:8983/solr/collection1/update?commit=true' --data-binary @example.json -H 'Content-type:application/json'

# FOTTIO di allarmi
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-06.json 
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-16.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-26.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-36.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-46.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-15-56.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-16-06.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-16-16.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-16-26.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-16-36.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-16-46.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-16-56.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-17-06.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-17-16.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-17-26.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-17-36.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-17-46.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-17-56.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-18-06.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-18-16.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-18-26.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-18-36.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-18-46.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-18-56.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-19-06.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-19-16.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-19-26.json
curl 'http://localhost:8983/solr/collection1/update?commit=true' -H 'Content-type:application/json' --data-binary @/home/edelpao/workspace/SOLR_ES/test/changes_create_svc-2-fmhistory_03-09-2020_13-19-36.json
