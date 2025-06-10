package Jun1Rok2023;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        System.err.println("Connecting...");
        try (Socket socket = new Socket(hostname,Server.DEFAULT_PORT);
             BufferedWriter o = new BufferedWriter(new OutputStreamWriter(System.out));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        ){
            System.err.println("Connected!");
            String line = in.readLine();
            o.write(line);
            o.newLine();
            o.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
