package likeDislike;
import java.util.Base64;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Database {
	String dbname, collection_name, MONGO_URL;
	MongoClientURI uri;
	MongoClient m;
	MongoDatabase db;
	MongoCollection<Document> collection;
	DBCursor d;
	public void setUristring(String uristring) {
		this.MONGO_URL = uristring;
	}
	//constructor 
	public Database(String uristring, String dbname, String collection) {
		this.dbname = dbname;
		this.collection_name = collection;
		this.MONGO_URL = uristring;
	}
	//setters and getters 
	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getCollection() {
		return collection_name;
	}

	public void setCollection(String collection) {
		this.collection_name = collection;
	}

	public void connectToDatabase()//to connect to mongo db with url, and collection name
	{
		uri = new MongoClientURI(this.MONGO_URL);

		m = new MongoClient(uri);
		db = m.getDatabase(uri.getDatabase());

		collection = db.getCollection(this.collection_name);
	}
	public void closeDb(){
		m.close();//to close the database
	}

	public int Like()//to like a location 
	{
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name", "Library IC1");//find the current location of the user 
		FindIterable<Document> cursor = collection.find(whereQuery);

		String result="";
		Document doc = cursor.first();
		result = doc.get("likes").toString();//get the number of likes currently stored in the database
		int noOfLikes = Integer.valueOf(result);//convert to integer 


		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("likes", noOfLikes+1));//update the number of likes by incrementing likes 

		BasicDBObject searchQuery = new BasicDBObject().append("name", "Library IC1");

		collection.updateOne(searchQuery, newDocument);

		cursor = collection.find(whereQuery);

		doc = cursor.first();
		result = doc.get("likes").toString();//get the number of likes again from the database 
		noOfLikes = Integer.valueOf(result);
		return (noOfLikes);//return likes 
	}

	public int DisLike()//to dislike a locatiom 
	{
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name", "Library IC1");//find the current location of the user 
		FindIterable<Document> cursor = collection.find(whereQuery);

		String result="";
		Document doc = cursor.first();
		result = doc.get("dislikes").toString();//get the number of dislikes currently stored in the database
		int noOfDislikes = Integer.valueOf(result);


		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("dislikes", noOfDislikes+1));//update the number of dislikes in the db by incrementing dislikes 

		BasicDBObject searchQuery = new BasicDBObject().append("name", "Library IC1");

		collection.updateOne(searchQuery, newDocument);

		cursor = collection.find(whereQuery);

		doc = cursor.first();
		result = doc.get("dislikes").toString();
		noOfDislikes = Integer.valueOf(result);
		return (noOfDislikes);//return the current number of dislikes in the database 
	}

	//to get both the number of likes and dislikes in the database and return as a document 
	public Document getStats()
	{
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name", "Library IC1");//find the current location of the user 
		FindIterable<Document> cursor = collection.find(whereQuery);
		Document doc = cursor.first();//return object as document 
		return doc;
	}


}