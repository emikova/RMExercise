package Jun1Rok2023;

import Echo.ClientHandlerRunnable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int DEFAULT_PORT = 1111;

    public static void main(String[] args) {
        try ( ServerSocket server  = new ServerSocket(DEFAULT_PORT);){
            System.out.println("Server bounded to " + DEFAULT_PORT);
            while (true){
                System.out.println("Listening...");
                try {
                    Socket client = server.accept();
                    System.out.println("Client accepted");
                    new Thread(new ClientRunnable(client)).start();



                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
