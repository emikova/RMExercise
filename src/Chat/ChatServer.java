package Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatServer {
    public static final int SERVER_TEST_PORT = 2233;

    public static void main(String[] args) {
        ChatServer server = new ChatServer(SERVER_TEST_PORT);
        server.execute();
    }

    private int port;
    private Set<UserThread> users = new HashSet<>();


    public ChatServer(int port){
        this.port = port;
    }

    private void execute(){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.err.println("Listening...");

            while (true){
                Socket client = serverSocket.accept();
                System.err.println("Client connected!");

                UserThread user = new UserThread(client,this);
                users.add(user);
                user.start();


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getUserNames() {
        return this.users.stream().map(UserThread::getNickname).collect(Collectors.toList());
    }

    public void broadcast(String s, UserThread except) {
        this.users.stream()
                .filter(u -> u != except)
                .forEach(u -> {
                    if (u != except) {  // Prevent sending to the sender
                        u.sendMEssage(s);
                    }
                });
    }


    public void remove(UserThread userThread) {
        String userName = userThread.getNickname();
        this.users.remove(userThread);
        System.err.println("Client disconnected " + userName);

    }
}
