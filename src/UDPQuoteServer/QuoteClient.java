package UDPQuoteServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class QuoteClient {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()){
            byte[] buf = new byte[256];
            InetAddress address = InetAddress.getLocalHost();

            socket.setSoTimeout(5000);

            DatagramPacket request = new DatagramPacket(buf, buf.length,address,QuoteServer.PORT);
            socket.send(request);

            DatagramPacket response = new DatagramPacket(buf,buf.length);
            socket.receive(response);

            String recv = new String(response.getData(),0, response.getLength(), StandardCharsets.UTF_8);
            System.out.println("Quote: " + recv);


        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
