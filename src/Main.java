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
        System.out.println("Hello World");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/").build();

        DbxClientV2 dbx = new DbxClientV2(config, "sl.BEa2iBGLvjxe_KoebnlSEvxhBFyWej8oohNsocyREqDmxrhJQZqNWZemkmIHW60gtDYF-9jTfPoqZkxUl5j4N_fcdt8Q-GDe07z57d0QphzigR1LovNfOxhVjdIB7toyrHBrRKNJgPan");

        FullAccount account = null;
        try {
            account = dbx.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(account.getName().getDisplayName());

        ListFolderResult result = dbx.files().listFolder("");
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
            FileMetadata metadata = dbx.files().uploadBuilder("/test.txt")
                    .uploadAndFinish(in);
        }

    }
}
