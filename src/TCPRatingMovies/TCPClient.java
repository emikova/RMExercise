package TCPRatingMovies;

import Jun1Rok2023.Server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            SocketAddress address = new InetSocketAddress("localhost", TCPServer.PORT);
            boolean connected = socketChannel.connect(address);

            Selector selector = Selector.open();

            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()){
                    SelectionKey key =  iterator.next();
                    iterator.remove();

                    if (key.isConnectable()){
                        if (socketChannel.finishConnect()){
                            System.err.println("Connected to server!");
                            key.interestOps(SelectionKey.OP_READ);
                        }else {
                            socketChannel.close();
                            System.err.println("Connection lost!");
                        }
                    }else if (key.isReadable()){
                        handleReading(key);
                    }else if (key.isWritable()){
                        handleWriting(key);
                    }
                }
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static void handleReading(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        int bytesRead = socketChannel.read(buffer);

        if (bytesRead == -1) {
            System.err.println("Server closed the connection!");
            key.cancel();
            socketChannel.close();
            return;
        }

        buffer.flip();
        String messageFromServer = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(messageFromServer);

        if (messageFromServer.contains("Goodbye!")) {
            System.err.println("Disconnected from server.");
            key.cancel();
            socketChannel.close();
            System.exit(1);// Properly close the connection
            return;
        }

        if (messageFromServer.contains("continue") || messageFromServer.contains("rate")) {
            key.attach("WAIT_FOR_CONTINUE");
        } else {
            key.attach(null);
        }

        key.interestOps(SelectionKey.OP_WRITE);
    }


    private static void handleWriting(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(512);

        BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
        String movieChoice = clientInput.readLine();
        //System.err.println(movieChoice);

        buffer.put(movieChoice.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.flip();

        System.err.println("Reply sent to server!");
        key.interestOps(SelectionKey.OP_READ);


    }

}
