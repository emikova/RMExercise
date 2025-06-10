package UDPEcho;

import Echo.EchoServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServerEcho extends Thread implements AutoCloseable{
    public static final int PORT = 1212;
    private final DatagramSocket socket;

    ServerEcho () throws SocketException {
        this.socket = new DatagramSocket(PORT);

    }

    @Override
    public void start() {
        try {
            while (true){
                byte[] buf = new byte[512];
                try {
                    DatagramPacket request = new DatagramPacket(buf, buf.length);
                    this.socket.send(request);

                    DatagramPacket response = new DatagramPacket(buf,buf.length,request.getAddress(),request.getPort());
                    this.socket.send(response);

                    String res = new String(request.getData(), 0 , request.getLength(), StandardCharsets.UTF_8);
                    if (res.equalsIgnoreCase("end")){
                        System.err.println("End!");
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }finally {
            this.socket.close();
        }
    }


    @Override
    public void close() throws Exception {
        this.socket.close();
    }
}
