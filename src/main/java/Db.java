import java.sql.Connection;
import java.sql.DriverManager;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Db {
    private static Db db = null;
    MongoDatabase conn;

    private Db(){
        String uri = "<connection string uri>";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");
            Document doc = collection.find(eq("title", "Back to the Future")).first();
            System.out.println(doc.toJson());
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
}
