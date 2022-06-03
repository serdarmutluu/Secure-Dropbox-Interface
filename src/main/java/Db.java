import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

import com.dropbox.core.v2.files.DeleteResult;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Db {
    private static Db db = null;
    MongoDatabase conn;

    private Db(){
        String uri = "mongodb+srv://BMB4016:bmb4016@cluster0.yhakc.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("bmb4016");
            this.conn = database;
        }
    }

    static MongoDatabase getDb(){
        if(db != null){
            return db.conn;
        }
        else{
            db = new Db();
        }
        return db.conn;
    }

    static void deleteFromDb(String path){
        String uri = "mongodb+srv://BMB4016:bmb4016@cluster0.yhakc.mongodb.net/?retryWrites=true&w=majority";
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            database = mongoClient.getDatabase("bmb4016");
            MongoCollection file = database.getCollection("file");
            file.findOneAndDelete(new Document().append("name",path));
        }

    }
    static void addToDb(String path, String key, List<String> users){
        String uri = "mongodb+srv://BMB4016:bmb4016@cluster0.yhakc.mongodb.net/?retryWrites=true&w=majority";
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            database = mongoClient.getDatabase("bmb4016");
            MongoCollection file = database.getCollection("file");
            try{
                file.findOneAndDelete(new Document().append("name", path).append("key",key).append("users", users));
            }catch (Exception e){

            }
            file.insertOne(new Document("_id", new ObjectId()).append("name", path).append("key", key).append("users", users));
        }
    }

    static String hasPermission(String user,String file){
        String uri = "mongodb+srv://BMB4016:bmb4016@cluster0.yhakc.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("bmb4016");
            MongoCollection fileCollection = database.getCollection("file");
            FindIterable<Document> fi  =  fileCollection.find(
                    eq("name", file));
            Document doc = fi.first();

            Object[] documentRes = doc.values().toArray();
            String key = (String) documentRes[2];
            List<String> users = (List<String>) documentRes[3];
            for(int i = 0;i< users.size();i++){
                if(users.get(i).equals(user)){
                    System.out.println(key);
                    return key;
                }
            }
        }
        return "-";
    }
}
