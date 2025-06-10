package Chargen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServer {
    public static int DEFAULT_PORT = 19000;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(DEFAULT_PORT);
            Scanner scanner = new Scanner(System.in);) {
            while (true){
                System.err.println("Listening...");
                Socket client = server.accept();
                System.err.println("Accepted");

                try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))){
                    String line;
                    while (true){
                        System.err.println("Enter a message: ");
                        line = scanner.nextLine();
                        out.write(line);
                        out.newLine();
                        out.flush();
                        System.err.println("Sent");

                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
