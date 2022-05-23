import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomString {
    public RandomString(){

    }
    public static String generate(){
        Random random = ThreadLocalRandom.current();
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        String encoded = Base64.getUrlEncoder().encodeToString(randomBytes);
        return encoded;
    }
}
