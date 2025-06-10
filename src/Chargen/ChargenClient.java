package Chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class ChargenClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 19000;

        try {
            SocketAddress address = new InetSocketAddress(host, port);
            SocketChannel client = SocketChannel.open(address);

            WritableByteChannel out = Channels.newChannel(System.out);

            ByteBuffer buffer = ByteBuffer.allocate(74);
            client.configureBlocking(false);

            while (true){
                int n = client.read(buffer);
                if (n>0){
                    buffer.flip();
                    out.write(buffer);
                    buffer.clear();
                }else if (n == -1){
                    break;
                } else if (n == 0) {
                    //System.err.println("Nothing");

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
