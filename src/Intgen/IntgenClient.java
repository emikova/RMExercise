package Intgen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

public class IntgenClient {
    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("localhost",IntgenServer.DEFAULT_PORT);

        try (SocketChannel client = SocketChannel.open(address)){
            ByteBuffer buffer = ByteBuffer.allocate(4);
            IntBuffer view = buffer.asIntBuffer();

            for (int expected = 0; ; expected++){
                while (buffer.hasRemaining()){
                    client.read(buffer);
                }

                int received = view.get();


                buffer.clear();
                view.rewind();

                if (received!=expected){
                    System.err.println("Error ❌");
                    break;
                }

                System.out.println(received);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
