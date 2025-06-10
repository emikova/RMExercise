package UDPEcho;

import Echo.EchoClient;
import Echo.EchoServer;

import java.io.IOException;
import java.net.SocketException;

public class EchoTest {
    public static void main(String[] args) {
        try (ServerEcho server = new ServerEcho();
             ClientEcho client = new ClientEcho();

        ){
            server.start();

            String echo;
            echo = client.sendEcho("t1 hello");
            System.out.println(echo);
            echo = client.sendEcho("t2 hello");
            System.out.println(echo);

            client.sendEcho("end");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.err.println("End!");
    }
}
