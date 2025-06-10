package DayTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.net.ConnectException;

public class DayTimeClient {
    public static void main(String[] args) {
        String hostName = "localhost";
        System.err.println("Connecting to " + hostName);


        try(Socket socket = new Socket(hostName, DayTimeServer.DEFAULT_PORT)) {
            System.err.println("Connected");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
                String time = in.readLine();
                System.out.println(time);
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
