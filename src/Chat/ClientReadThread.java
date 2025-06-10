package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReadThread extends Thread {
    private String name;
    private BufferedReader fromServer;
    public ClientReadThread(String name, Socket socket) {
        this.name = name;
        try {
            this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run(){
        while (true){
            try {
                String response = fromServer.readLine();

                if (response == null){
                    return;
                }

                System.out.println("\r" +  response);
                System.out.printf(":\r[%s]:",this.name);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
