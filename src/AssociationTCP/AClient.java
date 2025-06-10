package AssociationTCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class AClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            InetSocketAddress address = new InetSocketAddress("localhost",AServer.PORT);
            boolean accepted = socketChannel.connect(address);


            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {

                    } else if (key.isWritable()) {
                        
                    }
                }
            }

    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}