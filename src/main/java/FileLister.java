import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

public class FileLister {
    static void list(DbxClientV2 dbx,String path) throws DbxException {
        ListFolderResult result = dbx.files().listFolder(path);
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if(metadata instanceof FolderMetadata){
                    list(dbx,metadata.getPathLower());
                }
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                return;
            }

            result = dbx.files().listFolderContinue(result.getCursor());
        }
    }

    static void delete(DbxClientV2 dbx,String path,String pathToLook) throws DbxException {
        ListFolderResult result = dbx.files().listFolder(pathToLook);
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if(metadata.getPathLower().equals(path)){
                    dbx.files().deleteV2(metadata.getPathLower());
                    Db.deleteFromDb(metadata.getName());
                }
            }

            for (Metadata metadata : result.getEntries()) {
                if(metadata instanceof FolderMetadata){
                    delete(dbx,path,metadata.getPathLower());
                }
            }

            if (!result.getHasMore()) {
                return;
            }

            result = dbx.files().listFolderContinue(result.getCursor());
        }
    }




}
