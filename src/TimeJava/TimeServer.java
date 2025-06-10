package TimeJava;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeServer {
    public static final int DEFAULT_PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(DEFAULT_PORT)){
            while (true){
                try (Socket client = server.accept()){
                    Date now = new Date();
                    long msSince1970 =  now.getTime();
                    long secondsSince1970 = msSince1970/1000;
                    long secondsSince1900 = secondsSince1970 + TimeClient.diff;


                    byte[] time = new byte[4];
                    time[0] = (byte) (secondsSince1900>>24);
                    time[1] = (byte) (secondsSince1900>>16);
                    time[2] = (byte) (secondsSince1900>>8);
                    time[3] = (byte) (secondsSince1900);

                    BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
                    out.write(time);
                    out.flush();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
