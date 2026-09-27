import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Waits for a client to connect, sends it server_file.txt, then closes
 * the connection. Only one client is handled at a time (no threads).
 * The server keeps running after each client so it can be tested with
 * telnet and then MyClient without restarting it. Stop it with Ctrl+C.
 *
 * Usage: java MyServer <port>
 */
public class MyServer {

    private static final String FILE_NAME = "server_file.txt";

    // Checks the port argument and starts the server.
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MyServer <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        runServer(port);
    }

    // Opens the server socket on the given port and waits for clients.
    // Each client is handled completely before the next one is accepted.
    private static void runServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                try {
                    sendFile(clientSocket);
                } catch (IOException e) {
                    System.out.println("Error sending file: " + e.getMessage());
                }

                clientSocket.close();
                System.out.println("Connection closed");
            }
        } catch (IOException e) {
            // A BindException ends up here if the port is already in use
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // Reads server_file.txt and writes it to the client's socket in chunks.
    private static void sendFile(Socket clientSocket) throws IOException {
        System.out.println("Sending " + FILE_NAME + " to the client...");

        OutputStream socketOutput = clientSocket.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        int totalBytesSent = 0;

        try (FileInputStream fileInput = new FileInputStream(FILE_NAME)) {
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                socketOutput.write(buffer, 0, bytesRead);
                totalBytesSent += bytesRead;
            }
        }

        socketOutput.flush();
        System.out.println("Finished sending " + totalBytesSent + " bytes");
    }
}
