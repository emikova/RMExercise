package TimeJava;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class TimeClient {
    public static final long diff = 2208988800L;
    public static void main(String[] args) {
        String hostname = "localhost";

        try (Socket socket = new Socket(hostname, TimeServer.DEFAULT_PORT);
             BufferedInputStream in = new BufferedInputStream(socket.getInputStream());


        ){
            long secondsSince1900 = 0;

            for (int i = 0; i < 4 ; i++){
                secondsSince1900 = (secondsSince1900<<8)|in.read();
            }

            long secondsSince1970 = secondsSince1900-diff;
            long msSince1970 = secondsSince1970 * 1000;
            Date now = new Date(msSince1970);


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
