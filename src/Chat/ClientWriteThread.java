package Chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriteThread extends Thread{
    private String name;
    private PrintWriter toServer;
    public ClientWriteThread(String name, Socket socket){
        this.name = name;
        try {
            toServer = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        toServer.println(this.name);
        toServer.flush();

        try (Scanner scan = new Scanner(System.in)){
            String text;
            do {
                System.out.printf("\r[%s]: " , this.name);
                text = scan.nextLine();
                toServer.println(text);
                toServer.flush();
            }while (!text.equalsIgnoreCase("bye"));

        }
    }
}
