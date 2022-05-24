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

import javax.crypto.Cipher;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    static synchronized void download(DbxClientV2 dbx, String path) throws DbxException {
        String userHomeDir = System.getProperty("user.home");
        String dPath = "";
        File f = new File(userHomeDir  +"/dropbox");
        if(f.isDirectory()){

        }
        else {
            f.mkdir();
            dPath = userHomeDir + "/dropbox";
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
                        File i = new File(dPath + metadata.getPathLower());
                        if (!i.getParentFile().exists())
                            i.getParentFile().mkdirs();
                        if (!i.exists())
                            i.createNewFile();
                        FileOutputStream out = new FileOutputStream(i.getPath());
                        System.out.println(dPath + metadata.getName());
                        downloader.download(out);
                        out.close();
                        if(!Db.hasPermission(dbx.users().getCurrentAccount().getEmail(), metadata.getName()).equals("-")){
                            String key = Db.hasPermission(dbx.users().getCurrentAccount().getEmail(), metadata.getName());
                            System.out.println(key);
                            File o = new File(dPath+metadata.getPathLower());
                            File inp = new File(dPath+metadata.getPathLower());
                            FileProcessor.fileProcessor(2, key,inp,o);
                        }
                    } catch (DbxException | FileNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("TEST1");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(dPath + metadata.getPathLower());
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
                try{for (int i = 0; !sourcePath.substring(i,i+8).equals("/dropbox");i++){
                    arr[i] = ' ';
                }}catch (Exception e){
                    try{arr = arr.toString().replace("/dropbox"," ").split(" ")[1].toCharArray();}
                    catch (Exception exception) {
                        arr = ("/" + new File(sourcePath).getName()).toCharArray();
                    }
                }
                String upPath = new String(arr).trim();
                System.out.println(upPath);
                FileMetadata metadata = dbx.files().uploadBuilder(upPath)
                        .uploadAndFinish(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }
        }

    }


    static void upload(DbxClientV2 dbx,String sourcePath,List<String> users) throws DbxException {
        File f = new File(sourcePath);
        if(f.isDirectory()){
            char[] arr = sourcePath.toCharArray();
            try{for (int i = 0; !sourcePath.substring(i,i+8).equals("/dropbox");i++){
                arr[i] = ' ';
            }}catch (Exception e){
                try{arr = arr.toString().replace("/dropbox"," ").split(" ")[1].toCharArray();}
                catch (Exception exception){
                    String userHomeDir = System.getProperty("user.home");
                    try{if((new File(sourcePath).getPath()).substring(0,userHomeDir.length()-1).equals(userHomeDir)){
                        arr = ( new File(sourcePath).getPath()).substring(userHomeDir.length()-1).toCharArray();
                    }}
                    catch (Exception ek){
                        arr = ( new File(sourcePath).getName()).toCharArray();
                    }
                }
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
            String key = RandomString.generate();
            FileProcessor.fileProcessor(Cipher.ENCRYPT_MODE,key,inp,out);
            System.out.println(key);
            try (InputStream in = new FileInputStream(sourcePath)) {
                char[] arr = sourcePath.toCharArray();
                try{for (int i = 0; !sourcePath.substring(i,i+8).equals("/dropbox");i++){
                    arr[i] = ' ';
                }}catch (Exception e){
                    try{arr = arr.toString().replace("/dropbox"," ").split(" ")[1].toCharArray();}
                    catch (Exception exception){
                        String userHomeDir = System.getProperty("user.home");
                        try{ arr = (new File(sourcePath).getPath().replace(userHomeDir, " ").split(" ")[1]).toCharArray();}
                        catch(Exception exception1)
                        {arr = ( new File(sourcePath).getPath()).toCharArray();}
                    }
                }
                String upPath = new String(arr).trim();
                System.out.println(upPath);
                FileMetadata metadata = dbx.files().uploadBuilder(upPath)
                        .uploadAndFinish(in);
                List<MemberSelector> newMembers = new ArrayList<MemberSelector>();
                MemberSelector newMember;
                newMember = MemberSelector.email(dbx.users().getCurrentAccount().getEmail());
                newMembers.add(newMember);
                for(int i = 0;i<users.size();i++){
                    newMember = MemberSelector.email(users.get(i));
                    newMembers.add(newMember);
                }
                List<FileMemberActionResult> fileMemberActionResults = dbx.sharing().addFileMember(upPath, newMembers);
                Db.addToDb(inp.getName(), key,users);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }

            FileProcessor.fileProcessor(Cipher.DECRYPT_MODE,key,inp,out);

        }

    }

    static void delete(DbxClientV2 dbx,String path) throws DbxException {
        String dPath = System.getProperty("user.home") + "/dropbox";
        FileLister.delete(dbx,path,"");
        FileLister.deleteFromDisk(dPath,path);
    }

}
