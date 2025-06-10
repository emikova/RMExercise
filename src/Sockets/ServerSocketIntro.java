package Sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketIntro {

    public static void main(String[] args) {
        int port = 9000;
        //one option for creating a serversocket by creating an object of class ServerSocket with mentioning port rightahead
        try (ServerSocket server = new ServerSocket(port)){
            Socket client = server.accept();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //another option for creating a serversocket by creating an object of class ServerSocket without port, it's useful if we don't know port beforehand
        try (ServerSocket server = new ServerSocket()){
            server.bind(new InetSocketAddress(port));
            while (true){
                Socket client = server.accept();
                serve(client);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void serve(Socket client){

    }
}
