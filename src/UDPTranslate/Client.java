package UDPTranslate;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Client {
    private static final int BUF_SIZE = 1024;
    private static final int PORT = 3394;
    private static final String HOSTNAME = "localhost";

    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket()){
            InetAddress host = InetAddress.getByName(HOSTNAME);
            while (true){
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
                String word = in.readLine();
                byte[] messageToServer = word.getBytes(StandardCharsets.UTF_8);
                DatagramPacket send = new DatagramPacket(messageToServer,messageToServer.length,host,PORT);
                ds.send(send);

                DatagramPacket receiveFromServer = new DatagramPacket(new byte[BUF_SIZE],BUF_SIZE);
                ds.receive(receiveFromServer);
                String messageFromServer = new String(receiveFromServer.getData(),0, receiveFromServer.getLength(), StandardCharsets.UTF_8);
                out.write(messageFromServer);
                out.newLine();
                out.flush();

                if (word.equalsIgnoreCase("bye")){
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
