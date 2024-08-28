package com.alarm.purge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;

public class EsPurge {
	
	private static String INDEX_NAME_PREFIX = "fm_history";

	/**
	 * Returns size of all FM indices in bytes
	 */
	private static Long getAllFmIndicesSize(Map<String, Object> map) throws UnsupportedOperationException, IOException{
		Long sizeInBytes =(Long) ((Map)((Map)((Map)map.get("_all")).get("total")).get("store")).get("size_in_bytes");
		return sizeInBytes;
	}
	
	private static String getSizeBasedToBeDeletedIndices(RestHighLevelClient client, int maxSizeLimitInGb)
			throws UnsupportedOperationException, IOException{
		StringBuffer tbdIndices = new StringBuffer();
		Map<String, Object> map;
		
		map = getAllFmIndicesStats(client);
		Long allIndicesSize = getAllFmIndicesSize(map);
		log("FM indices size: " + allIndicesSize);
		
		Long maxSizeLimit = maxSizeLimitInGb * 1024*1024*1024L;
		//Check if allIndicesSize is below limit. If not, get the indices 
		//and sizes in sorted order and find older indices to be deleted
		if(allIndicesSize > maxSizeLimit)
		{
			long size = allIndicesSize;
			Map<String, Long> sortedMap = getSortedMap(map);
			Set<Entry<String, Long>> entries = sortedMap.entrySet();
			Iterator<Entry<String, Long>> itr = entries.iterator();
			Entry<String, Long> entry;
			while(itr.hasNext()){
				entry = itr.next();
				size = size - entry.getValue();
				log("Marking index " + entry.getKey() + " of size " + entry.getValue() + " to delete");
				if (tbdIndices.length() != 0)
					tbdIndices.append(",");
				tbdIndices.append(entry.getKey());
				
				if(size <= maxSizeLimit)
					break;
			}
		}else{
			log("FM alarm history data size is below the size limit");
		}
		
		return tbdIndices.toString();
	}
	
	private static Map<String, Long> getSortedMap(Map<String, Object> map){
		Map<String, Object> indices = (Map<String, Object>) map.get("indices");
		Set<Entry<String, Object>> keys = indices.entrySet();
		Iterator<Entry<String, Object>> itr = keys.iterator();
		Map<String, Long> sortedMap = new TreeMap<String, Long>(new Comparator<String>() {
			private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

			@Override
			public int compare(String index1, String index2) {
				try {
					long t1 = getTime(index1);
					long t2 = getTime(index2);
					long diff = t1 - t2;
					return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			private long getTime(String indexName) throws ParseException{
				Date dt;
				if(indexName.contains("-"))
					dt = dateFormat.parse(indexName.substring(indexName.lastIndexOf("-") + 1));
				else
					dt = dateFormat.parse(indexName.substring(indexName.lastIndexOf("_") + 1));
				return dt.getTime();
			}
		});
		
		Entry<String, Object> entry;
		while(itr.hasNext()) {
			entry = itr.next();
			Map<String, Object> mapx = (Map<String, Object>) entry.getValue();
			Object obj = ((Map)((Map)mapx.get("total")).get("store")).get("size_in_bytes");
			sortedMap.put(entry.getKey(), Long.parseLong(obj.toString()));
		}
		
		return sortedMap;
	}
	
	private static String getTimeBasedToBeDeletedIndices(RestHighLevelClient client, int days)
			throws ParseException, UnsupportedOperationException, IOException{
		Map<String, Object> map = getAllFmIndicesStats(client);
		Map<String, Object> indices = (Map<String, Object>) map.get("indices");
		Set<String> keys = indices.keySet();
		Iterator<String> itr = keys.iterator();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1 * days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		StringBuffer tbdIndices = new StringBuffer();
		String indexName;
		Date dt;
		while(itr.hasNext()){
			indexName = itr.next();
			if(indexName.contains("-"))
				dt = dateFormat.parse(indexName.substring(indexName.lastIndexOf("-") + 1));
			else
				dt = dateFormat.parse(indexName.substring(indexName.lastIndexOf("_") + 1));
			if(dt.before(cal.getTime())) {
				if (tbdIndices.length() != 0)
					tbdIndices.append(",");
				tbdIndices.append(indexName);
			}
		}
		log("Total number of indices: " + indices.size());
		return tbdIndices.toString();
	}
	
	private static Map<String, Object> getAllFmIndicesStats(RestHighLevelClient client)
			throws UnsupportedOperationException, IOException{
		
		String rq = INDEX_NAME_PREFIX + "*/_stats/store";
		Request statsReq = new Request("GET", rq);
		Response response = client.getLowLevelClient().performRequest(statsReq);
		
		Map<String, Object> map = null;
		try (InputStream is = response.getEntity().getContent()) {
			map = XContentHelper.convertToMap(XContentType.JSON.xContent(), is, true);
		}
		return map;
	}
	
	private static boolean deleteIndices(RestHighLevelClient client, String indices) throws IOException{
		
		//Success response: {"acknowledged":true}
		/*Exception on failure to delete indices when index not found:
		 * org.elasticsearch.client.ResponseException: method [DELETE], host [http://10.247.246.8:8900], URI
		 * [<comma_separated_indices>], status line [HTTP/1.1 404 Not Found]
		 */
		boolean response = false;
		Request delReq = new Request("DELETE", indices);
		Response delResp = client.getLowLevelClient().performRequest(delReq);
		
		try (InputStream is = delResp.getEntity().getContent()) {
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(reader);
			StringBuffer sb = new StringBuffer();
			String str;
			while((str = br.readLine())!= null){
			   sb.append(str);
			}
			log("Delete response: " + sb.toString());
			//TODO Check: response should be {"acknowledged":true}
			response = true;
		}catch(ResponseException ex){
			ex.printStackTrace();
			response = false;
		}
		return response;
	}

	/**
	 * args[0] 0-> TimeBasedPurge, 1-> SizeBasedPurge
	 * args[1] Number of days to preserve   or   Max size limit for FM indices
	 * args[2] ES IP
	 * args[3] ES Port
	 * args[4] 0-> Disable purging, 1-> Enable Purging
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		
		log("EsPurge " + args[0]+" " + args[1] + " " + args[2] + " " + args[3] + " " + args[4]);
		
		boolean sizeBasedPurge = false;
		if (args[0].equals("1")){
			sizeBasedPurge = true;
			log("Invoked Size based purging");
		}else{
			log("Invoked Time based purging");
		}
		
		RestHighLevelClient client = null;
		try {

			client = new RestHighLevelClient(
					RestClient.builder(new HttpHost(args[2], Integer.parseInt(args[3]),"http")));
			
			String tbdIndices;
			if(sizeBasedPurge){
				tbdIndices = getSizeBasedToBeDeletedIndices(client, Integer.parseInt(args[1]));
				log("Size based purging: ToBeDeletedIndices: " + tbdIndices);
			}else{
				tbdIndices = getTimeBasedToBeDeletedIndices(client, Integer.parseInt(args[1]));
				log("Time based purging: ToBeDeletedIndices: " + tbdIndices);
			}
			
			if(args[4].equals("1")){
				log("Purging ES Indices");
				deleteIndices(client, tbdIndices);  //TODO
			}
			
//			System.out.println("Deleting " + args[1]);
//			deleteIndices(client, args[1]);		//For testing
			
		}
		finally{
			if (client != null) client.close();
		}
	}
	
	private static void log(String msg){
		System.out.println(new Date() + ": " + msg);
	}
}
