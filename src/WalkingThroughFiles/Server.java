package WalkingThroughFiles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import WalkingThroughFiles.FileTreeWalker;

public class Server {
    private static final int PORT = 1212;
    private static final int BUF_SIZE = 1024;

    public static void main(String[] args) {
        try (DatagramSocket datagramSocket = new DatagramSocket(PORT)){
            while (true) {
                DatagramPacket fromClient = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
                datagramSocket.receive(fromClient);

                String messageFromClient = new String(fromClient.getData(), 0, fromClient.getLength());

                if (messageFromClient.equalsIgnoreCase("bye")){
                    System.err.println("Client disconnected!❌");
                    continue;
                }
                String folderPath = "C:\\Users\\User\\IdeaProjects\\RMExercise\\src\\WalkingThroughFiles";
                String[] parts = messageFromClient.split("-");
                if (parts.length == 2) {
                    String fileContent = readRequestedFile(folderPath, parts[0], parts[1]);
                    if (fileContent != null) {
                        byte[] toClient = fileContent.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket sendToClient = new DatagramPacket(toClient, toClient.length, fromClient.getAddress(), fromClient.getPort());
                        datagramSocket.send(sendToClient);
                        System.err.println("Message sent to client!");
                    } else {
                        System.err.println("No such file found!");
                    }
                }
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readRequestedFile(String folderPath, String messageFromClient,String requestedData) {
        BlockingQueue<Path> queue = new LinkedBlockingQueue<>();
        Path startDir = Paths.get(folderPath);

        Thread walker = new Thread(new FileTreeWalker(startDir,queue));
        walker.start();

        String requestedFromClient = messageFromClient + ".txt";
        String result = null;

        try {
            while (true){
                Path path = queue.take();
                if (path.equals(FileTreeWalker.END_OF_WORK)){
                    break;
                }

                if (path.getFileName().toString().equalsIgnoreCase(requestedFromClient)){
                    List<String> lines = Files.readAllLines(path);
                    for (String line:lines){
                        if (line.contains(requestedData)){
                            result = line;
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;

    }
}
