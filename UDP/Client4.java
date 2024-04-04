package UDP;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client4 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9999;
    private static final int CLIENT_PORT = 9998;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(CLIENT_PORT)) {
            System.out.println("Connected to server.");

            System.out.print("Enter your username: ");
            Scanner userInput = new Scanner(System.in);
            String userName = userInput.nextLine();

            sendHelloMessage(userName, socket);

            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            while (true) {
                String userInputLine = userInput.nextLine();
                sendMessage(userName + ": " + userInputLine, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendHelloMessage(String userName, DatagramSocket socket) throws IOException {
        String helloMessage = "hello:" + userName;
        byte[] sendData = helloMessage.getBytes();
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
        socket.send(sendPacket);
    }

    private static void sendMessage(String message, DatagramSocket socket) throws IOException {
        byte[] sendData = message.getBytes();
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
        socket.send(sendPacket);
    }
}

