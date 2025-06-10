package WalkingThroughFiles;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws SocketException {
        try (DatagramSocket datagramSocket = new DatagramSocket()){
            while (true){
                InetAddress address = InetAddress.getByName("localhost");
                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter a file name:");
                String clientInput = scanner.nextLine();

                if (clientInput.equalsIgnoreCase("bye")){
                    String byeMessage = "bye";
                    byte[] byeBytes = byeMessage.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket byeToServer = new DatagramPacket(byeBytes,byeBytes.length,address,1122);
                    datagramSocket.send(byeToServer);
                    System.err.println("Disconnected from server!❌");
                    datagramSocket.close();
                    break;
                }

                byte[] data = clientInput.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendToServer = new DatagramPacket(data,data.length,address,1212);
                datagramSocket.send(sendToServer);


                DatagramPacket fromServer = new DatagramPacket(new byte[1024],1024);
                datagramSocket.receive(fromServer);

                String messageFromServer = new String(fromServer.getData(),0, fromServer.getLength());
                System.err.println("Server: " + messageFromServer);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
