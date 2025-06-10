import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StreamReader {
    private static final String URL_STRING  = "https://sr.wikipedia.org/wiki/Massive_Attack";

    public static void main(String[] args) throws IOException {
        URL u = new URL(URL_STRING);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()))){
            String line;
            while ((line = in.readLine())!= null){
                System.out.println(line);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        URLConnection connection = u.openConnection();
        String encoding = connection.getContentEncoding();
        if (encoding == null){
            encoding = "UTF-8";
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),encoding))){
            String line;
            while ((line = in.readLine()) != null){
                System.out.println(line);
            }

        }
    }
}
