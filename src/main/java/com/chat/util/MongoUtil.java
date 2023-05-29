package com.chat.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * mongodb工具类
 */
@Component
@Data
public class MongoUtil {
	static String host;
	static String port;
	static String database;

	static MongoDatabase mongoDatabase;

	static {
		ResourceBundle rb = ResourceBundle.getBundle("application");
		host = rb.getString("spring.data.mongodb.host");
		port = rb.getString("spring.data.mongodb.port");
		database = rb.getString("spring.data.mongodb.database");
		String ip = "mongodb://" + host + ":" + port;
		MongoClient mongoClient = MongoClients.create(ip);
		mongoDatabase = mongoClient.getDatabase(database);
	}

	/**
	 * 获取数据库集合对象
	 * @param collection
	 * @return
	 */
	static public MongoCollection<Document> getDatabase(String collection) {
		return mongoDatabase.getCollection(collection);
	}
}
