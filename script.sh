

start=`date +%s`
/usr/bin/curl http://localhost:39982/solr/collection1/query -d "q=*:*&start=20000&rows=1000&fq=insertTime:[2020-07-29T03:00:00.000Z TO 2020-07-29T03:59:59.999Z]&sort=insertTime+asc" | jq -c '.response["docs"]' > /tmp/zetto1

end=`date +%s`
echo Execution time was `expr $end - $start` seconds.
