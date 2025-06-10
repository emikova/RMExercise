package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
    private Socket socket;
    private ChatServer chatServer;
    private String name;
    private PrintWriter toUSer;
    public UserThread(Socket socket, ChatServer chatServer){
        this.socket = socket;
        this.chatServer= chatServer;
    }

    @Override
    public void run(){
        try {
            this.toUSer = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader fromUser = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            this.name = fromUser.readLine();
            this.sendMEssage("Connected users: " + chatServer.getUserNames());

            this.chatServer.broadcast("New user connected: " + this.name,this);


            String clientMessage ;
            do {
                clientMessage = fromUser.readLine();
                if (clientMessage == null){
                    break;
                }
                this.chatServer.broadcast("[" + this.name + "] " + clientMessage, this);

            }while (!clientMessage.equalsIgnoreCase("bye"));

            chatServer.remove(this);
            this.socket.close();
            this.chatServer.broadcast(this.name + " has left ", this);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMEssage(String s) {
        if (this.toUSer != null){
            this.toUSer.println(s);
        }
    }

    public String getNickname(){
        return this.name;
    }


}
