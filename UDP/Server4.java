package UDP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server4 {
    private static final int PORT = 9999;
    private static List<InetAddress> clientAddresses = new ArrayList<>();
    private static int clientPort = 9998;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Server started...");
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                if (!clientAddresses.contains(clientAddress)) {
                    clientAddresses.add(clientAddress);
                }
                
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + message);
                broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            byte[] sendData = message.getBytes();
            for (InetAddress clientAddress : clientAddresses) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

