import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;

public class DownloadWorker implements Runnable{
    String path;
    DbxClientV2 dbx;
    public DownloadWorker(DbxClientV2 dbx,String path){
        this.dbx = dbx;
        this.path = path;
    }


    @Override
    public void run() {
        System.out.println("Started " + path);
        try {
            FileManager.download(dbx,path);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        System.out.println("Ended " + path);
    }
}
