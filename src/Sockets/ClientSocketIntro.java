package Sockets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocketIntro {
    public static void main(String[] args) {
        try (Socket socket = new Socket("hostname",80)){
            socket.getInputStream(); //reading what server sent
            socket.getOutputStream(); //writing to server

            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

            // socket.close() isn't required because of the try ()
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
