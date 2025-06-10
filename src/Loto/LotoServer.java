package Loto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class LotoServer {
    public static final int PORT_SERVER = 1111;

    public static void main(String[] args) {
        int[] combination = new int[7];

        int i = 0;
        Random random = new Random();
        while (i<7){
            int number = random.nextInt(39) + 1;
            if (Arrays.stream(combination).anyMatch(u->u==number)){
                continue;
            }
            combination[i] = number;
            i++;
        }

        Arrays.sort(combination);
        for (int num: combination){
            System.out.println(num + " ");
        }
        System.out.println();

        try (ServerSocketChannel server = ServerSocketChannel.open();
             Selector selector = Selector.open();

        ){


            if (!server.isOpen()){
                System.err.println("!open");
                System.exit(1);
            }

            server.bind(new InetSocketAddress(PORT_SERVER));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    try {
                        if (key.isAcceptable()){
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel client = serverSocketChannel.accept();
                            System.out.println("Client accepted!");

                            client.configureBlocking(false);
                            SelectionKey clientKey = client.register(selector,SelectionKey.OP_READ);

                            ByteBuffer buffer = ByteBuffer.allocate(7*4);
                            clientKey.attach(buffer);

                        } else if (key.isReadable()) {
                            SocketChannel client =(SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            client.read(buffer);

                            if (!buffer.hasRemaining()){
                                buffer.flip();

                                int result = 0;
                                while (buffer.hasRemaining()){
                                    int number = buffer.getInt();
                                    if (Arrays.stream(combination).anyMatch(u->u==number)){
                                        result++;
                                    }
                                }
                                buffer.clear();
                                buffer.putInt(result);
                                buffer.flip();

                                key.interestOps(SelectionKey.OP_WRITE);
                            }

                        }else if (key.isWritable()){
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            client.write(buffer);

                            if (!buffer.hasRemaining()){
                                client.close();
                                System.out.println("Finished!");
                            }
                        }
                    }catch (IOException e){
                        key.cancel();
                        try {
                            key.channel().close();
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }

                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
