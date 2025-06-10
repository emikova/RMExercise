package DayTime;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DayTimeServer {
    public final static int DEFAULT_PORT = 8765;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(DEFAULT_PORT)){
            System.err.println("Server bound to port!" + DEFAULT_PORT );
            while (true){
              try (Socket client =  server.accept()){
                  System.err.println("Client accepted!");

                  try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))){
                      Date now = new Date();
                      out.write(now.toString());
                      out.newLine();
                      out.flush();
                  }
                  System.err.println("Client handled!");
              }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
