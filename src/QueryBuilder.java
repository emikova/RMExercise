import java.util.Random;

public class QueryBuilder {
    public static void main(String[] args) {
        int electronics = new Random().nextInt(500) + 1;
        String endpoint = "https://api.restful-api.dev/objects" + electronics + "/";

    }
}
