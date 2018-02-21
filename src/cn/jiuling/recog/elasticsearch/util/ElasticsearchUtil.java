package cn.jiuling.recog.elasticsearch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.loocme.sys.util.ListUtil;

public class ElasticsearchUtil{

    private static TransportClient client = null;

    public static void init(String clusterName, int port, String... hosts){
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        client = new PreBuiltTransportClient(settings);
        if (null != hosts) 
        try{
            for (int i = 0; i < hosts.length; i++){
                client.addTransportAddress(new InetSocketTransportAddress( InetAddress.getByName(hosts[i]), port));
            }
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public static boolean indexResultData(String index, String type,String rowkey, Map<String, Object> source){
        if (null == client) return false;
        IndexResponse response = client.prepareIndex(index, type, rowkey).setSource(source).get();
        if (response.status().getStatus() == 201) return true;
        return false;
    }

    public static boolean indexResultDataBulk(String index, List<String> rowkeys, List<Map<String, Object>> sources){
        if (null == client) return false;
        if (ListUtil.isNull(rowkeys)) return true;
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int i = 0; i < rowkeys.size(); i++){
            bulkRequest.add(client.prepareIndex(index, "simple", rowkeys.get(i)).setSource(sources.get(i)));
        }
        BulkResponse response = bulkRequest.execute().actionGet();
        if (response.status().getStatus() == 200) return true;
        return false;
    }

    public static Map<String, Object> scrollScarResultData(String[] indexNames,BoolQueryBuilder boolBuilder){
        if (null == client) return null;
        Map<String, Object> retMap = new HashMap<String, Object>();
        SearchRequestBuilder responsebuilder = client.prepareSearch(indexNames).setTypes("simple").setScroll(new TimeValue(60000));
        SearchResponse myresponse = responsebuilder.setQuery(boolBuilder).setSize(2000).get();
        SearchHits hits = myresponse.getHits();
        retMap.put("total", hits.getTotalHits());
        retMap.put("scrollId", myresponse.getScrollId());
        List<String> resultIds = new ArrayList<String>();
        for (int i = 0; i < hits.getHits().length; i++){
            resultIds.add(hits.getHits()[i].getId());
            System.out.println(hits.getHits()[i].getSourceAsString());
        }
        retMap.put("resultIds", resultIds);
        return retMap;
    }
    
    public static Map<String, Object> scrollScarResultData(String[] indexNames, BoolQueryBuilder boolBuilder, int maxCount,String sorting) { //count 指代导出的条数
		if (null == client) return null;

		Map<String, Object> retMap = new HashMap<String, Object>();
		SearchRequestBuilder responsebuilder = client.prepareSearch(indexNames).setTypes("simple").setScroll(new TimeValue(60000));
		SearchResponse myresponse = responsebuilder.setQuery(boolBuilder).setSize(maxCount).addSort("createtime", sorting.equals("desc") ? SortOrder.DESC : SortOrder.ASC).get();
		SearchHits hits = myresponse.getHits();
		//retMap.put("total", hits.getTotalHits());
		//retMap.put("scrollId", myresponse.getScrollId());
		List<String> resultIds = new ArrayList<String>();
		for (int i = 0; i < hits.getHits().length; i++) {
			resultIds.add(hits.getHits()[i].getId());
		}
		retMap.put("resultIds", resultIds);
		return retMap;
	}

    public static List<String> scrollScarResultData(String scrollId){
        if (null == client) return null;
        SearchScrollRequestBuilder searchScrollRequestBuilder = client.prepareSearchScroll(scrollId);
        searchScrollRequestBuilder.setScroll(new TimeValue(60000));
        SearchResponse myresponse = searchScrollRequestBuilder.get();
        SearchHits hits = myresponse.getHits();
        List<String> resultIds = new ArrayList<String>();
        for (int i = 0; i < hits.getHits().length; i++){
            resultIds.add(hits.getHits()[i].getId());
        }
        return resultIds;
    }

    /**
     * 清除滚动ID
     * 
     * @param client
     * @param scrollId
     * @return
     */
    public static boolean clearScroll(String scrollId){
        ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
        clearScrollRequestBuilder.addScrollId(scrollId);
        ClearScrollResponse response = clearScrollRequestBuilder.get();
        return response.isSucceeded();
    }
    /**
     * 清空单个索引
     * @param index
     * @return
     */
    public static boolean deleteResultDataIndex(String index){
        if (null == client) return false;
        if (!indexExists(index)){
            return true;
        }
        DeleteIndexResponse response = client.admin().indices().prepareDelete(index).execute().actionGet();
        return response.isAcknowledged();
    }
    /**
     * 清空所有索引（慎点）
     */
    public static  void cleanAllIndex(){
    	ClusterStateResponse response = client.admin().cluster().prepareState().execute().actionGet();  
    	String[] indexs=response.getState().getMetaData().getConcreteAllIndices();  
        for (String index : indexs) {  
            System.out.println(index+" delete");//  
             //清空所有索引。  
            client.admin().indices().prepareDelete(index).execute().actionGet();    
        }  
    }
    /**
     * 创建索引
     * @param indexName
     */
    public static void createResultIndex(String indexName){
        if (null == client) return;
        String path = ElasticsearchUtil.class.getClassLoader().getResource("tables/"+indexName+".txt").getPath();
        List<String> columns = text2List(new File(path));
        if (indexExists(indexName)){
            return;
        }
        try{
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("simple").startObject("properties").startObject("id").field("type", "integer").endObject();
            for(String str : columns){
            	String[] params = str.split(",");
            	String columnName = params[0];
            	String columnType = params[1];
            	String analyzeType = params[2];
            	builder.startObject(columnName).field("type",columnType).field("index",analyzeType).endObject();
            }
            builder.endObject().endObject().endObject();
            client.admin().indices().prepareCreate(indexName).addMapping("simple", builder).get();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public  static List<String> text2List(File file){
    	List<String> result = new ArrayList<String>();
	    try   
	    {       
	        if(file.isFile()&&file.exists())  
	        {       
	            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");       
	            BufferedReader reader=new BufferedReader(read);       
	            String line;       
	            while ((line = reader.readLine()) != null)   
	            {        
	            	result.add(line);    
	            }         
	            read.close();      
	        }     
	    } catch (Exception e)   
	    {         
	        e.printStackTrace();     
	    }     
	    return result;   
    }
    /**
     * 判断集群中{index}是否存在
     * 
     * @param index
     * @return 存在（true）、不存在（false）
     */
    public static boolean indexExists(String index)
    {
        if (null == client) return false;

        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        if (response.isExists()){
            return true;
        }
        return false;
    }

    public static void close(){
        if (null == client) client.close();
    }

    public static TransportClient getClient(){
        return client;
    }
    
    /**
     * 获取总数
     * @param indexName 索引名称
     * @param boolBuilder 条件
     * @return
     */
    public static long countResultData(String indexName,BoolQueryBuilder boolBuilder){
        if (null == client) return 0;
        SearchRequestBuilder responsebuilder = client.prepareSearch(indexName).setTypes("simple");
        SearchResponse myresponse = responsebuilder.setQuery(boolBuilder).get();
        return myresponse.getHits().getTotalHits();
    }
    
    /* private final static int SCROLL_SIZE = 1000;
    
    public static Page<String> pageResultData(String[] ymdArr, BoolQueryBuilder boolBuilder, int pageNum, int pageSize,String sorting)
    {
        Page<String> page = new Page<String>(pageNum,pageSize);
        List<String> resultIdList = new ArrayList<String>();
        if (null == client) return page;
        
        int startNum = (pageNum - 1) * pageSize;
        SearchRequestBuilder responsebuilder = client
                .prepareSearch(ymdArr).setTypes("simple").setScroll(new TimeValue(60000));
        SearchResponse myresponse = responsebuilder.setQuery(boolBuilder)
                .setSize(SCROLL_SIZE).addSort("createtime", sorting.equals("desc") ? SortOrder.DESC : SortOrder.ASC).get();
        page.setTotalCount(((Long) myresponse.getHits().getTotalHits()).intValue());
        String scrollId = myresponse.getScrollId();
        SearchScrollRequestBuilder searchScrollRequestBuilder = null;
        while (startNum >= SCROLL_SIZE)
        {
            searchScrollRequestBuilder = client
                    .prepareSearchScroll(scrollId);
            searchScrollRequestBuilder.setScroll(new TimeValue(60000));
            myresponse = searchScrollRequestBuilder.get();
            if (myresponse.getHits().getHits().length == 0)
                return page;
            startNum -= SCROLL_SIZE;
        }
        
        SearchHits hits = myresponse.getHits();
        for (int i = startNum; i < hits.getHits().length; i++)
        {
            resultIdList.add(hits.getHits()[i].getId());
            pageSize --;
            if (0 == pageSize)break;
        }
        
        if (0 != pageSize)
        {
            searchScrollRequestBuilder = client
                    .prepareSearchScroll(scrollId);
            searchScrollRequestBuilder.setScroll(new TimeValue(60000));
            myresponse = searchScrollRequestBuilder.get();
            hits = myresponse.getHits();
            for (int i = 0; i < hits.getHits().length; i++)
            {
                resultIdList.add(hits.getHits()[i].getId());
                pageSize --;
                if (0 == pageSize)break;
            }
        }
        clearScroll(scrollId);
        
        page.setResult(resultIdList);
        
        return page;
    }
    */
    public static void main(String[] args)
    {
    	
        init("sxt", 9300, "119.29.214.134");
       /* cleanAllIndex();
        createResultIndex("tec_content");*/
        //scrollScarResultData
        BoolQueryBuilder queryBuilder2 = QueryBuilders.boolQuery();
        queryBuilder2.must(QueryBuilders.matchPhraseQuery("titletype",3));
        //String[] indexNames = {"tec_content"};
       // scrollScarResultData(indexNames,queryBuilder2);
        System.out.println(countResultData("tec_content",queryBuilder2));
        		//.must(QueryBuilders.termQuery("note", "test1"));
        
        close();
        
        
    }
}
