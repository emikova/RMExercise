import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class CopyingImageFromNet {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://cms.piknicelectronik.com/uploads/NEWS/OFF-PIKNIC24_OUTILS-COMM_SITE-WEB_VISUELS-ARTISTES_09-26_BORIS-BREJCHA.png");
            URLConnection connection = url.openConnection();

            String contentType = connection.getContentType();
            int contentLength = connection.getContentLength();

            if (contentLength == -1 || contentType.startsWith("text")){
                throw new IOException("It's not a bin file");
            }

            BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf('/')+1);
            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName))){
                for (int i = 0; i < contentLength; i++){
                    int b = input.read();
                    out.write(b);
                }

            }
        }catch (IOException e){
            System.err.println("Error");
        }
    }
}
