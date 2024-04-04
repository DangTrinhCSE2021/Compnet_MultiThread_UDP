package UDP;

import java.net.*;
import java.util.Scanner;

public class Client5 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9999;
    private static final int CLIENT_PORT = 9998;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(CLIENT_PORT)) {
            System.out.println("Connected to server.");

            Scanner userInput = new Scanner(System.in);
            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            while (true) {
                System.out.print("Enter your guess: ");
                int guess = userInput.nextInt();
                byte[] sendData = Integer.toString(guess).getBytes();
                InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                socket.send(sendPacket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


