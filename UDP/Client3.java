package UDP;

import java.io.*;
import java.net.*;

public class Client3 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readLine();
                        if (message == null) {
                            break;
                        }
                        System.out.println("Server: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                out.println(userInputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

