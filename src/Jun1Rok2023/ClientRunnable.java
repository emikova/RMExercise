package Jun1Rok2023;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientRunnable implements Runnable{
    private Socket client;
    public ClientRunnable(Socket client){
        this.client = client;
    }

    @Override
    public void run(){
        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream(), StandardCharsets.UTF_8));
        ){
            out.write("Welcome HO");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
