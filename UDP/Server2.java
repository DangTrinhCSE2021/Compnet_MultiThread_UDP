package UDP;

import java.io.*;
import java.net.*;

public class Server2 {
    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        
        try {
            serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            System.out.println("Server started...");
            
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                
                // Extracting data from the received packet
                ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
                ObjectInputStream ois = new ObjectInputStream(bis);
                int[] numbers = (int[]) ois.readObject();
                
                // Calculating the least common multiple
                int lcm = calculateLCM(numbers[0], numbers[1], numbers[2]);
                
                // Sending the result back to the client
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(lcm);
                byte[] sendData = bos.toByteArray();
                
                InetAddress clientIPAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    private static int calculateLCM(int a, int b, int c) {
        return lcm(lcm(a, b), c);
    }

    private static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}

