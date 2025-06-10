package Loto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Scanner;

public class LotoClient {
    public static void main(String[] args) {
        try (SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost",LotoServer.PORT_SERVER));
             Scanner scanner = new Scanner(System.in);
        ){
            ByteBuffer buffer = ByteBuffer.allocate(7*4);

            int[] combination = new int[7];
            int i = 0;
            while (i<7){
                System.out.println("Enter a number: ");
                int number = scanner.nextInt();

                if (number<1 ||number > 39){
                    continue;
                }

                if (Arrays.stream(combination).anyMatch(u->u==number)){
                    continue;
                }
                combination[i] = number;
                i++;
            }

            for (int num : combination){
                buffer.putInt(num);
            }

            buffer.flip();
            client.write(buffer);

            buffer.clear();
            client.read(buffer);

            buffer.flip();
            int result = buffer.getInt();
            System.out.println("Reuslt: " + result);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
