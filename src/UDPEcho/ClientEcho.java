package UDPEcho;

import Echo.EchoServer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientEcho  implements AutoCloseable{
    private final DatagramSocket socket;
    private final InetAddress address;

    ClientEcho() throws SocketException {
        this.socket = new DatagramSocket();
        this.address = InetAddress.getLoopbackAddress();
    }

    String sendEcho(String message) throws IOException {
        byte[] buf = message.getBytes(StandardCharsets.UTF_8);
        System.err.println("Client sent:" + message + " " + Arrays.toString(buf));
        DatagramPacket request = new DatagramPacket(buf, buf.length, this.address, ServerEcho.PORT);
        this.socket.send(request);

        DatagramPacket received = new DatagramPacket(buf, buf.length);
        this.socket.receive(received);
        System.err.println("Client received: " + Arrays.toString(received.getData()));

        return  new String(received.getData(), 0 , received.getLength(), StandardCharsets.UTF_8);

    }


    @Override
    public void close() throws Exception {
        this.socket.close();
    }
}
