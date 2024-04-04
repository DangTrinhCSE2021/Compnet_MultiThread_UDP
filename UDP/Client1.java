package UDP;

import java.io.*;
import java.net.*;

public class Client1 {
    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        
        try {
            clientSocket = new DatagramSocket();
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            InetAddress serverIPAddress = InetAddress.getByName("localhost");
            
            byte[] sendData;
            byte[] receiveData = new byte[1024];
            
            while (true) {
                System.out.print("Enter a string: ");
                String sentence = inFromUser.readLine();
                sendData = sentence.getBytes();
                
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, 9876);
                clientSocket.send(sendPacket);
                
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                System.out.println("From Server: " + modifiedSentence);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
