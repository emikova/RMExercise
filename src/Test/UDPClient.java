package Test;


import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPClient {
    private static final int PORT = 12321;
    private static final int BUF_SIZE = 1024;
    private static final String HOSTNAME = "localhost";
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket()){
            InetAddress host = InetAddress.getByName(HOSTNAME);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
            try(BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    String in = input.readLine();
                    byte[] data = in.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket send = new DatagramPacket(data, data.length, host, PORT);
                    ds.send(send);
                    if (in.equalsIgnoreCase("bye")){
                        break;
                    }

                    DatagramPacket receive = new DatagramPacket(new byte[BUF_SIZE],BUF_SIZE);
                    ds.receive(receive);
                    String messageReceived = new String(receive.getData(),0, receive.getLength(),StandardCharsets.UTF_8);
                    out.write(messageReceived);
                    out.newLine();
                    out.flush();

                }
            }



        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
