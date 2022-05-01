import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;
import java.nio.file.DirectoryStream;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws DbxException, IOException {
        String userHomeDir = System.getProperty("user.home");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/").build();
        DbxAppInfo appInfo  = new DbxAppInfo("aed4jqlgklt5iek","4okiktgmoqgiu50");
        DbxAuthFinish authFinish = null;
        authFinish = new PkceAuthorize().authorize(appInfo);
        System.out.println("Authorization complete.");
        System.out.println("- User ID: " + authFinish.getUserId());
        System.out.println("- Account ID: " + authFinish.getAccountId());
        System.out.println("- Access Token: " + authFinish.getAccessToken());
        System.out.println("- Expires At: " + authFinish.getExpiresAt());
        System.out.println("- Refresh Token: " + authFinish.getRefreshToken());
        System.out.println("- Scope: " + authFinish.getScope());
        String token = authFinish.getAccessToken();
        DbxClientV2 dbx = new DbxClientV2(config, token);

        FullAccount account = null;
        try {
            account = dbx.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(account.getName().getDisplayName());
        System.out.println(dbx.users());
        FileLister.list(dbx,"");
        DbxDownloader<FileMetadata> downloader = dbx.files().download("/dedem/document.docx");
        try {
            FileOutputStream out = new FileOutputStream("document.docx");
            downloader.download(out);
            out.close();
        } catch (DbxException ex) {
            System.out.println(ex.getMessage());
        }
        // Upload "test.txt" to Dropbox
        FileManager.upload(dbx,"test.txt","/dedem/test.txt");



    }
}
