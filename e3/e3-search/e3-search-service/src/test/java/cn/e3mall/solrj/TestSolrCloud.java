package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {

	//@Test
	public void testAddDocument() throws Exception{
		//创建一个集群的连接，应该使用CloudSolrServer创建
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.129:2181,192.168.25.129:2182,192.168.25.129:2183");
		//zkHost：zookeeper的地址列表
		//设置一个defaultCollection属性
		cloudSolrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档添加域
		document.setField("id", "idtest");
		document.setField("item_title", "titletest");
		document.setField("item_price", 111);
		//把文档写入索引库
		cloudSolrServer.add(document);
		//提交
		cloudSolrServer.commit();
	}
	
	//@Test
	public void testSelectDocument() throws Exception{
		//创建一个集群的连接，应该使用CloudSolrServer创建
				CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.129:2181,192.168.25.129:2182,192.168.25.129:2183");
				//zkHost：zookeeper的地址列表
				//设置一个defaultCollection属性
				cloudSolrServer.setDefaultCollection("collection2");
				//创建一个查询对象
				SolrQuery query = new SolrQuery();
				//设置查询条件
				query.setQuery("*:*");
				//执行查询条件
				QueryResponse response = cloudSolrServer.query(query);
				//取查询结果
				SolrDocumentList list = response.getResults();
				//打印
				System.out.println("总记录数："+list.getNumFound());
				for(SolrDocument document : list){
					System.out.println("id:"+document.get("id"));
					System.out.println("title:"+document.get("title"));
					System.out.println("item_title:"+document.get("item_title"));
					System.out.println("price:"+document.get("item_price"));
				}
	}
}
