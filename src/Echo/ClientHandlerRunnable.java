package Echo;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandlerRunnable implements Runnable{
    private Socket client;
    public ClientHandlerRunnable(Socket client){
        this.client = client;
    }



    @Override
    public void run(){
        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream(),StandardCharsets.UTF_8));

        ){
            String s;
            while ((s = in.readLine()) != null){
                out.write(s);
                out.newLine();
                out.flush();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
