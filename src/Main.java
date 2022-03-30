import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws DbxException, IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/").build();
        /*String token = "sl.BExv5gV21qKK_5it4q6IWl30xCTtmRpy7RmVdJdGyBQalzfsclgX-m2TnzSJBhVRIDdj8PLH2tzy4KcY6iFSBEU6HC7X2DMa3Diqwq-5h5nLtWObqiZIQRhOzYGLz5z_Eozkug7hAXvw";
        DbxClientV2 dbx = new DbxClientV2(config, token);

        FullAccount account = null;
        try {
            account = dbx.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(account.getName().getDisplayName());
        System.out.println(dbx.users());
        ListFolderResult result = dbx.files().listFolder("/dedem");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = dbx.files().listFolderContinue(result.getCursor());
        }

        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("test.txt")) {
            FileMetadata metadata = dbx.files().uploadBuilder("/dedem/test.txt")
                    .uploadAndFinish(in);
        }*/
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

    }
}
