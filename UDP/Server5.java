package UDP;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server5 {
    private static final int SERVER_PORT = 9999;
    private static int targetNumber;
    private static final int TIMEOUT_SECONDS = 180;
    private static boolean gameWon = false;
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static List<InetAddress> clientAddresses = new ArrayList<>();
    private static int clientPort = 9998;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("Server started...");
            generateTargetNumber();
            System.out.println("Target number: " + targetNumber);
            // Start a timer to timeout if no correct prediction is made within TIMEOUT_SECONDS
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                if (!gameWon) {
                    sendToAllClients("Time out");
                    System.exit(0);
                }
            }, TIMEOUT_SECONDS, TimeUnit.SECONDS);

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
                int guess = Integer.parseInt(message.trim());
                if (guess == targetNumber) {
                    gameWon = true;
                    sendToClient(clientAddress, clientPort, "Congratulations! You guessed it right!");
                    sendToOtherClients(clientAddress, clientPort, "You lose");
                    System.exit(0);
                } else {
                    sendToClient(clientAddress, clientPort, "Please predict again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateTargetNumber() {
        targetNumber = new Random().nextInt(101); // Generate a random number between 0 and 100
    }

    private static void sendToClient(InetAddress clientAddress, int clientPort, String message) {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void sendToOtherClients(InetAddress clientAddress, int clientPort, String message) {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            byte[] sendData = message.getBytes();
            for (InetAddress address : clientAddresses) {
                if (!address.equals(clientAddress)) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, clientPort);
                    serverSocket.send(sendPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void sendToAllClients(String message) {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            byte[] sendData = message.getBytes();
            for (InetAddress address : clientAddresses) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

