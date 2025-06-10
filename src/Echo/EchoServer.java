package Echo;

import Sockets.ClientSocketIntro;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static final int DEFAULT_PORT = 4444;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(DEFAULT_PORT)){
            System.out.println("Started server on " + DEFAULT_PORT);

            while (true){
                System.out.println("Listening...");
                try {
                    Socket client = server.accept();
                    System.out.println("Client accepted");
                    new Thread(new ClientHandlerRunnable(client)).start();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
