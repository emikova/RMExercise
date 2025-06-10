package UDPProtocol;

import java.io.IOException;
import java.net.*;

public class UDPIntro {
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket()){
            InetAddress host = InetAddress.getByName("host");
            DatagramPacket send = new DatagramPacket(new byte[512],512,host,12345);
            DatagramPacket receive = new DatagramPacket(new byte[512],512);

            ds.send(send);
            ds.receive(receive);

            receive.getData();
            receive.getAddress();
            receive.getPort();

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
