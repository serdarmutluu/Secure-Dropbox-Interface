import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxUploader;
import com.dropbox.core.http.HttpRequestor;
import com.dropbox.core.oauth.DbxRefreshResult;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxRawClientV2;
import com.dropbox.core.v2.common.PathRoot;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.paper.Folder;
import com.dropbox.core.v2.sharing.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    static synchronized void download(DbxClientV2 dbx, String path) throws DbxException {
        String userHomeDir = System.getProperty("user.home");
        String dPath = "";
        File f = new File(userHomeDir  +"dropbox");
        if(f.isDirectory()){

        }
        else {
            f.mkdir();
            dPath = userHomeDir + "/dropbox/";
        }
        ListFolderResult result = dbx.files().listFolder(path);
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if(metadata instanceof FolderMetadata){
                    File file = new File(dPath + metadata.getPathLower());
                    if(file.isDirectory()){

                    }
                    else file.mkdir();
                    new DownloadWorker(dbx,metadata.getPathLower()).run();
                }
                else{
                    DbxDownloader<FileMetadata> downloader = dbx.files().download(metadata.getPathLower());
                    try {
                        FileOutputStream out = new FileOutputStream(dPath + metadata.getPathLower());
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

    static void upload(DbxClientV2 dbx,String sourcePath) throws DbxException {
        File f = new File(sourcePath);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i = 0;i<files.length;i++){
                upload(dbx,files[i].getPath());
            }
        }
        else{
            try (InputStream in = new FileInputStream(sourcePath)) {
                char[] arr = sourcePath.toCharArray();
                for (int i = 0; !sourcePath.substring(i,i+8).equals("/dropbox");i++){
                    arr[i] = ' ';
                }
                String upPath = new String(arr).trim();
                FileMetadata metadata = dbx.files().uploadBuilder(upPath)
                        .uploadAndFinish(in);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }
        }

    }


    static void upload(DbxClientV2 dbx,String sourcePath,String[] users) throws DbxException {
        File f = new File(sourcePath);
        if(f.isDirectory()){
            char[] arr = sourcePath.toCharArray();
            for (int i = 0; !sourcePath.substring(i,i+8).equals("/dropbox");i++){
                arr[i] = ' ';
            }
            String upPath = new String(arr).trim();
            File[] files = f.listFiles();
            for(int i = 0;i<files.length;i++){
                upload(dbx,files[i].getPath(),users);
            }

        }
        else{
            File out = new File(sourcePath);
            File inp = new File(sourcePath);
            FileProcessor.fileProcessor(1,"gVkYp3s6v9y$B?E(",inp,out);
            try (InputStream in = new FileInputStream(sourcePath)) {
                char[] arr = sourcePath.toCharArray();
                for (int i = 0; !sourcePath.substring(i,i+8).equals("/dropbox");i++){
                    arr[i] = ' ';
                }
                String upPath = new String(arr).trim();
                FileMetadata metadata = dbx.files().uploadBuilder(upPath)
                        .uploadAndFinish(in);
                List<MemberSelector> newMembers = new ArrayList<MemberSelector>();
                MemberSelector newMember;
                for(int i = 0;i<users.length;i++){
                    newMember = MemberSelector.email(users[i]);
                    newMembers.add(newMember);
                }
                List<FileMemberActionResult> fileMemberActionResults = dbx.sharing().addFileMember(upPath, newMembers);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }
            FileProcessor.fileProcessor(2,"gVkYp3s6v9y$B?E(",inp,out);

        }

    }

}
