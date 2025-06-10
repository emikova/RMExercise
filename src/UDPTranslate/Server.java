package UDPTranslate;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int BUF_SIZE = 1024;
    private static final int PORT = 3394;

    public static void main(String[] args) {
        Map<String,String> words = new HashMap<>();
        String textFile = "C:\\Users\\User\\IdeaProjects\\RMExercise\\src\\UDPTranslate\\dictionary.txt";
        try (BufferedReader in = new BufferedReader(new FileReader(textFile))){
            String line;
            while ((line = in.readLine()) != null){
                String[] parts = line.split(":");
                if (parts.length == 2){
                    String Serbian = parts[0].trim();
                    String English = parts[1].trim();
                    words.put(Serbian,English);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (DatagramSocket ds = new DatagramSocket(PORT)){
            while (true){
                DatagramPacket receiveFromClient = new DatagramPacket(new byte[BUF_SIZE],BUF_SIZE);
                ds.receive(receiveFromClient);

                String messageFromClient = new String(receiveFromClient.getData(), 0, receiveFromClient.getLength(), StandardCharsets.UTF_8);
                System.out.println(messageFromClient);

                String wordFound = null;
                for (Map.Entry<String,String> word: words.entrySet()){
                    if (word.getKey().equalsIgnoreCase(messageFromClient)){
                        wordFound = word.getValue();
                        break;
                    }
                }

                if (wordFound != null){
                    byte[] toClient = wordFound.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket sendToClient = new DatagramPacket(toClient, toClient.length, receiveFromClient.getAddress(), receiveFromClient.getPort());
                    ds.send(sendToClient);
                    System.err.println("Translated word send to client: " + wordFound);
                }else{
                    String errorMessageToClient = "Couldn't translate the word";
                    byte[] dataToClient = errorMessageToClient.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket errorToClient = new DatagramPacket(dataToClient,dataToClient.length,receiveFromClient.getAddress(), receiveFromClient.getPort());
                    ds.send(errorToClient);

                }


            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
