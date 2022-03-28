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
        String token = "sl.BEr8SM-PPnhbhxhhnSjyJ8689GMHorV1ElEgrZV3Vw2ZgMSnCxEgful3aueL7MEyf0tt2glo3n0gIfNaVIngg21jp7_3Lz6pNz6pAtUFvkD-7EHkK7sk-mB44kM3mGZWxX0o7h7ObUgg";
        DbxClientV2 dbx = new DbxClientV2(config, token);

        FullAccount account = null;
        try {
            account = dbx.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(account.getName().getDisplayName());

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
        }

    }
}
