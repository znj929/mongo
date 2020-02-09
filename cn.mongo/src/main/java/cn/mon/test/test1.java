package cn.mon.test;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import javax.annotation.Resource;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * java 简单操作mongodb
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class test1 {
	private static final Logger logger = LoggerFactory
            .getLogger(test1.class);
	
	private MongoDatabase mongoDatabase;
	
	//配置文件用的是mongoClient
	@Resource(name="mongoClient")
	private MongoClient mongoClient;
	
	private MongoCollection<Document> collection;
	
	@Before
	public void init() {
		mongoDatabase = mongoClient.getDatabase("znj");
		collection = mongoDatabase.getCollection("users");
		collection = mongoDatabase.getCollection("ordersTest");
	}
	
	/**
	 * 查找操作
	 * sql==>select * from 
	 * mongo==>db.users.find().pretty();
	 */
	@Test
	public void findTest(){
		/*
		 * Bson filter = eq("user1", "deer"); FindIterable<Document> find =
		 * collection.find(filter);
		 */
		FindIterable<Document> find = collection.find();
		MongoCursor<Document> iterator = find.iterator();
		while (iterator.hasNext()) {
			Document document = iterator.next();
			System.err.println(document);
		}
	}
	
	/**
	 * 插入操作
	 * sql==>insert into 
	 * mongo==>
	 * 		var user1 = deer;
	 * 		db.users.insert(user1);
	 */
	@Test
	public void insTest() {
		//向数据库的user集合中插入一个usename为lison的文档 
		String user1 = "lison";
		Integer age = 18;
		Document document = new Document();
		document.append("user1", user1);
		document.append("age", age);
		collection.insertOne(document);
	}
	
	/**
	 * 更新操作
	 * sql==>update set
	 * mongo==>
	 * 		db.users.updateMany()
	 */
	@Test
	public void updateTest() {
		Bson filter = eq("user1", "lison");//定义数据过滤器，user1 = 'lison'
		Bson update = set("age", 9);//更新的字段.来自于Updates包的静态导入
		UpdateResult updateOne = collection.updateMany(filter, update);
		//打印受影响的行数
		System.err.println("------------------>" + String.valueOf(updateOne.getModifiedCount()));
	}
	
	/**
	 * 删除操作
	 * sql==>delete from 
	 * mongo==>
	 * 		db.users.deleteMany();
	 */
	@Test
	public void delTest() {
		Bson filter = eq("user1","deer");
		DeleteResult deleteMany = collection.deleteMany(filter);
		//打印受影响的行数
		System.err.println("------------------>" + String.valueOf(deleteMany.getDeletedCount()));
	}
	
	
}
