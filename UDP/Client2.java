package UDP;

import java.io.*;
import java.net.*;

public class Client2 {
    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        
        try {
            clientSocket = new DatagramSocket();
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            InetAddress serverIPAddress = InetAddress.getByName("localhost");
            
            while (true) {
                // Input three integers from the user
                System.out.print("Enter three integers (separated by space): ");
                String[] input = inFromUser.readLine().split(" ");
                
                // Convert string input to integers
                int a = Integer.parseInt(input[0]);
                int b = Integer.parseInt(input[1]);
                int c = Integer.parseInt(input[2]);
                
                // Serialize the integers to send to the server
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(new int[]{a, b, c});
                byte[] sendData = bos.toByteArray();
                
                // Send data to the server
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, 9876);
                clientSocket.send(sendPacket);
                
                // Receive the result from the server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                
                // Extract the result from the received packet
                ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
                ObjectInputStream ois = new ObjectInputStream(bis);
                int lcm = (int) ois.readObject();
                
                System.out.println("Least Common Multiple: " + lcm);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}

