import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.mongodb.Mongo;
import com.mongodb.client.*;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.print.Doc;
import javax.swing.*;

import static com.mongodb.client.model.Filters.eq;

public class Main {
    public static void main(String[] args) throws DbxException, IOException {
        String userHomeDir = System.getProperty("user.home");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("/").build();
        DbxAppInfo appInfo  = new DbxAppInfo("babjneltp07d691","wq2088pimskcxyh");
        DbxAuthFinish authFinish = null;
        authFinish = new PkceAuthorize().authorize(appInfo);
        System.out.println("Authorization complete.");
        /*System.out.println("- User ID: " + authFinish.getUserId());
        System.out.println("- Account ID: " + authFinish.getAccountId());
        System.out.println("- Access Token: " + authFinish.getAccessToken());
        System.out.println("- Expires At: " + authFinish.getExpiresAt());
        System.out.println("- Refresh Token: " + authFinish.getRefreshToken());
        System.out.println("- Scope: " + authFinish.getScope());*/
        String token = authFinish.getAccessToken();
        String dPath ="";
        DbxClientV2 dbx = new DbxClientV2(config, token);

        File f = new File(userHomeDir  +"dropbox");
        if(f.isDirectory()){

        }
        else {
            f.mkdir();
            dPath = userHomeDir + "/dropbox";
        }

        FullAccount account = null;
        try {
            account = dbx.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(account.getName().getDisplayName());

        //System.out.println(dbx.users());
        //MongoDatabase c = Db.getDb();

        //FileLister.list(dbx,"");
        //FileManager.upload(dbx,dPath);

        // Upload "test.txt" to Dropbox
        List<String> users = new ArrayList<String>();
        users.add(dbx.users().getCurrentAccount().getEmail());
        users.add("031890066@ogr.uludag.edu.tr");
        DownloadWorker dw = new DownloadWorker(dbx,"");
        dw.run();
        /*String uri = "mongodb+srv://BMB4016:bmb4016@cluster0.yhakc.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("bmb4016");
            MongoCollection fileCollection = database.getCollection("file");
            Document d = (Document) fileCollection.find(eq("name","kickstart1.cpp")).first();
            String t = (String) d.values().toArray()[2];
        }
        DbxDownloader<FileMetadata> downloader = dbx.files().download("/kickstart1.cpp");
        FileOutputStream out = new FileOutputStream(dPath+"/kickstart1.cpp");
        downloader.download(out);
        out.close();
        if(!Db.hasPermission(dbx.users().getCurrentAccount().getEmail(), "kickstart1.cpp").equals("-")){
            String key = Db.hasPermission(dbx.users().getCurrentAccount().getEmail(), "kickstart1.cpp");
            System.out.println(key);
            File o = new File(dPath+"/kickstart1.cpp");
            File inp = new File(dPath+"/kickstart1.cpp");
            FileProcessor.fileProcessor(2, key,inp,o);
        }
        //FileManager.delete(dbx,"/kickstart1.cpp");*/
    }
}
