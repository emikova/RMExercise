package TCPRatingMovies;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TCPServer {
    public static final int PORT = 6677;
    private static Map<String, Float> movies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        movies = getMovies();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            if (!serverSocketChannel.isOpen() || !selector.isOpen()) {
                System.err.println("Error opening server or selector!");
                return;
            }

            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {

                        handleAccept(serverSocketChannel, selector);

                    } else if (key.isReadable()) {
                        handleReading(key,selector);
                    } else if (key.isWritable()) {
                        handleWriting(key);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleAccept(ServerSocketChannel server, Selector selector) throws IOException {
        SocketChannel client = server.accept();
        client.configureBlocking(false);

        System.err.println("Client accepted! 🤠");

        Map<String, Float> freshMovies = new HashMap<>();
        for (Map.Entry<String,Float> movie: movies.entrySet()){
            freshMovies.put(movie.getKey(),0.0f);
        }

        String welcomeMessage = "Welcome!\nRate these movies: " + freshMovies;
        ByteBuffer buffer = ByteBuffer.wrap(welcomeMessage.getBytes());

        while (buffer.hasRemaining()) {
            client.write(buffer);
        }

        client.register(selector, SelectionKey.OP_READ, freshMovies);
    }

    private static void handleReading(SelectionKey key, Selector selector) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        int bytesRead = client.read(buffer);

        if (bytesRead == -1) {
            key.cancel();
            client.close();
            System.err.println("Connection lost!");
            return;
        }

        buffer.flip();
        String receivedResponse = StandardCharsets.UTF_8.decode(buffer).toString().trim();  // Trim spaces
        System.err.println("Received from client: " + receivedResponse);
        buffer.clear();

        Map<String,Float> clientMovies = (Map<String, Float>) key.attachment();

        if (receivedResponse.equalsIgnoreCase("bye")) {
            ByteBuffer byeBuffer = ByteBuffer.wrap("Goodbye!".getBytes());
            client.write(byeBuffer);
            client.close();
            key.cancel();
            System.err.println("Client disconnected.");
            return;
        }

        if (receivedResponse.equalsIgnoreCase("continue")) {
            ByteBuffer continueBuffer = ByteBuffer.wrap(("Rate movies: " + clientMovies).getBytes());
            client.write(continueBuffer);
            return;
        }

        // Check if it's a valid rating input (should be "Movie Name, Rating")
        if (receivedResponse.contains(",")) {
            try {
                updateRating(receivedResponse, clientMovies);
                ByteBuffer responseBuffer = ByteBuffer.wrap("Rating updated. Type 'continue' to rate more or 'bye' to exit.".getBytes());
                client.write(responseBuffer);
            } catch (Exception e) {
                ByteBuffer errorBuffer = ByteBuffer.wrap("Invalid rating format! Use: 'Movie Name, Rating'.".getBytes());
                client.write(errorBuffer);
            }
        } else {
            ByteBuffer invalidCommandBuffer = ByteBuffer.wrap("Invalid input! Type 'continue' or 'bye'.".getBytes());
            client.write(invalidCommandBuffer);
        }
    }


    private static void handleWriting(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        String response = (String) key.attachment();

        if (response != null) {
            ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
            System.err.println("Response sent to client!");
        }

        key.attach(null);
        key.interestOps(SelectionKey.OP_READ);
    }

    public static void updateRating(String replyFromClient, Map<String,Float> clientMovies) {
        String[] parts = replyFromClient.split(",");
        if (parts.length < 2) {
            System.err.println("Invalid rating format!");
            return;
        }

        String movieName = parts[0].trim();
        float newRating;
        try {
            newRating = Float.parseFloat(parts[1].trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid rating value!");
            return;
        }

        if (clientMovies.containsKey(movieName)) {
            clientMovies.put(movieName, newRating);
            System.err.println("Updated new rating of " + movieName + " with " + newRating);
        } else {
            System.out.println("Movie not found. Available movies: " + clientMovies);
        }
    }

    public static Map<String, Float> getMovies() throws IOException {
        File filePath = new File("C:\\Users\\User\\IdeaProjects\\RMExercise\\src\\TCPRatingMovies\\movies.txt");
        Map<String, Float> movieMap = new HashMap<>();

        try (BufferedReader fromFile = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = fromFile.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String movieName = parts[0].trim();
                    float movieRating = Float.parseFloat(parts[1].trim());
                    movieMap.put(movieName, movieRating);
                }
            }
        }
        return movieMap;
    }
}
