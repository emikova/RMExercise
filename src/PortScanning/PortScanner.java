package PortScanning;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class PortScanner {
    public static void main(String[] args) {
        String host = "localhost";
        System.out.println("Start time: " + new Date());

        for (int port = 1; port<65536;port++){
            System.out.println("\rTesting port: " + port);

            try (Socket s = new Socket(host,port)){
                System.out.println("\rSocket data: " + s);
                System.out.println("Found: " + new Date());
            } catch (UnknownHostException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
