package UDPProtocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class DaytimeServer {
    private static final int PORT = 12345;
    private static final int BUF_SIZE = 1024;

    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(PORT)){
            while (true){
                DatagramPacket receive = new DatagramPacket(new byte[BUF_SIZE],BUF_SIZE);
                ds.receive(receive);
                System.err.println("Received!");

                String daytime = new Date().toString();
                byte[] data = daytime.getBytes(StandardCharsets.US_ASCII);
                DatagramPacket send = new DatagramPacket(data, data.length, receive.getAddress(),receive.getPort());
                ds.send(send);
                System.err.println("Sent!");
            }


        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
