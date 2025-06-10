package Echo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class EchoClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        System.err.println("Connecting");

        try (Socket socket = new Socket(hostname,EchoServer.DEFAULT_PORT);
             BufferedReader networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter networkOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
        ){

            System.err.println("Connected");
            while (true){
                String line = userIn.readLine();
                if (line.trim().equalsIgnoreCase("exit")) {
                    break;
                }

                networkOut.write(line);
                networkOut.newLine();
                networkOut.flush();

                System.out.println(networkIn.readLine());
            }


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
