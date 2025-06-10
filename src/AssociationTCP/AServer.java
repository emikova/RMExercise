package AssociationTCP;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class AServer {
    public static final int PORT = 9876;
    private static Map<String, List<String>> associations = new HashMap<>();

    public static void main(String[] args) throws IOException {
        associations = getAssociations();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open();
        ){

            if (!serverSocketChannel.isOpen() || !selector.isOpen()){
                System.err.println("Error opening server or selector!");
                return;
            }
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()){
                        handleAccept(serverSocketChannel,selector);
                    } else if (key.isReadable()) {

                    } else if (key.isWritable()) {

                    }
                }
            }

        }
    }

    private static void handleAccept( ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);


        System.err.println("Client accepted! 🤠");

        Map<String, List<String>> copyOfAss = new HashMap<>();
        for (Map.Entry<String, List<String>> association : associations.entrySet()){
            String keyword = association.getKey();
            List<String> words = association.getValue();

            copyOfAss.put(keyword,words);
        }

        System.err.println(copyOfAss);




    }


    private static Map<String, List<String>> getAssociations() throws IOException {
        File filePath = new File("C:\\Users\\User\\IdeaProjects\\RMExercise\\src\\AssociationTCP\\associations.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] parts = line.split(" ");
                if (parts.length == 5){
                    String keyWord = parts[4].trim();
                    List<String> words = new ArrayList<>(Arrays.asList(parts[0].trim(),parts[1].trim(),parts[2].trim(),parts[3].trim()));

                    associations.put(keyWord,words);
                }
            }
        }
        return associations;
    }
}