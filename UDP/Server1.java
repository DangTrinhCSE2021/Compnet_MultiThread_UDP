package UDP;

import java.io.*;
import java.net.*;

public class Server1 {
    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        
        try {
            serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            System.out.println("Server started...");
            
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + sentence);
                
                InetAddress clientIPAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                
                String capitalizedSentence = sentence.toUpperCase();
                byte[] sendData = capitalizedSentence.getBytes();
                
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

