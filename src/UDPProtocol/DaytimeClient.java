package UDPProtocol;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class DaytimeClient {
    private static final int PORT = 12345;
    private static final int BUF_SIZE = 1024;
    private static final String HOSTNAME = "localhost";
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket()){
            InetAddress host = InetAddress.getByName(HOSTNAME);
            DatagramPacket send = new DatagramPacket(new byte[1],1,host,PORT);
            DatagramPacket receive = new DatagramPacket(new byte[BUF_SIZE],BUF_SIZE);

            ds.send(send);
            System.err.println("Sent!");

            ds.receive(receive);
            System.err.println("Received!");

            String result = new String(receive.getData(),0,receive.getLength(), StandardCharsets.US_ASCII);
            System.out.println(result);





        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
