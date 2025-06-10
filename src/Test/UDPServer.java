package Test;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {
    private static final int PORT = 12321;
    private static final int BUF_SIZE = 1024;
    public static void main(String[] args) {
        String filePath = "C:\\Users\\User\\IdeaProjects\\RMExercise\\src\\Test\\country\\coutry.txt";
        Map<String,String> capitalCities = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = in.readLine()) != null){
                String[] parts = line.split(":");
                if (parts.length == 2){
                    String country = parts[0].trim();
                    String capitalCity = parts[1].trim();
                    capitalCities.put(country,capitalCity);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(capitalCities);


        try(DatagramSocket ds = new DatagramSocket(PORT)) {
            while (true) {
                DatagramPacket receive = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
                ds.receive(receive);

                String message = new String(receive.getData(), 0, receive.getLength(), StandardCharsets.UTF_8).trim();
                System.out.println(message);
                String countryFound = null;
                for (Map.Entry<String, String> entry : capitalCities.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(message)) {
                        countryFound = entry.getKey();
                        break;
                    }
                }

                // Send response
                if (countryFound != null) {
                    byte[] toClient = countryFound.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket send = new DatagramPacket(toClient, toClient.length, receive.getAddress(), receive.getPort());
                    ds.send(send);
                    System.out.println("Sent: " + countryFound);
                } else {
                    System.out.println("No match found for: " + message);
                    String errorMessage = "No match found for " + message;
                    byte[] data = errorMessage.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket sendError = new DatagramPacket(data,data.length,receive.getAddress(), receive.getPort());
                    ds.send(sendError);
                }

                if (message.trim().equalsIgnoreCase("bye")){
                    break;
                }



            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
