import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.*;

public class FileManager {
    static void download(DbxClientV2 dbx, String path) throws DbxException {
        ListFolderResult result = dbx.files().listFolder(path);
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if(metadata instanceof FolderMetadata){
                    FileManager.download(dbx,metadata.getPathLower());
                }
                else{
                    DbxDownloader<FileMetadata> downloader = dbx.files().download(metadata.getPathLower());
                    try {
                        FileOutputStream out = new FileOutputStream(metadata.getPathLower());
                        downloader.download(out);
                        out.close();
                    } catch (DbxException | FileNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!result.getHasMore()) {
                return;
            }

            result = dbx.files().listFolderContinue(result.getCursor());
        }
    }

    static void upload(DbxClientV2 dbx,String sourcePath, String targetPath){
        try (InputStream in = new FileInputStream(sourcePath)) {
            FileMetadata metadata = dbx.files().uploadBuilder(targetPath)
                    .uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
    }

}
