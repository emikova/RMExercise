package UDPQuoteServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class QuoteServer extends Thread{
    public static final int PORT = 12345;
    public static void main(String[] args) throws IOException {
        new QuoteServer().start();
    }

    private DatagramSocket socket;
    private BufferedReader in;

    QuoteServer() throws IOException {
        this.socket = new DatagramSocket(PORT);
        this.in = Files.newBufferedReader(Paths.get("C:\\Users\\User\\IdeaProjects\\RMExercise\\src\\UDPQuoteServer\\oneliners.txt"));
    }

    @Override
    public void run(){
        System.err.println("Listening...");
        try {
            while (true){
                byte[] buf = new byte[512];
                DatagramPacket received = new DatagramPacket(buf, buf.length);
                socket.receive(received);
                buf = getDataToSend();
                if (buf == null){
                    break;
                }
                DatagramPacket response = new DatagramPacket(buf,buf.length,received.getAddress(),received.getPort());
                socket.send(response);
                System.err.println("Sent!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            this.socket.close();
        }
    }

    private byte[] getDataToSend() throws IOException {
        String data = this.in == null? new Date().toString(): this.in.readLine();
        return data == null? null: data.getBytes();
    }


}
