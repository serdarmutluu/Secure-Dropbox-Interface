import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.mongodb.Mongo;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws DbxException, IOException {
        String userHomeDir = System.getProperty("user.home");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/").build();
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
        DownloadWorker dw = new DownloadWorker(dbx,"");
        dw.run();
//        // Upload "test.txt" to Dropbox
        List<String> users = new ArrayList<String>();
        users.add(dbx.users().getCurrentAccount().getEmail());
        users.add("031890066@ogr.uludag.edu.tr");
        //FileManager.upload(dbx,userHomeDir + "/kickstart.py",users);


    }
}
