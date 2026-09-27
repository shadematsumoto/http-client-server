import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/*
 * Connects to MyServer on localhost, reads the file the server sends,
 * and saves it as client_file.txt.
 *
 * Usage: java MyClient <port>
 */
public class MyClient {

    private static final String HOST = "localhost";
    private static final String FILE_NAME = "client_file.txt";

    // Checks the port argument, connects to the server and saves the file.
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MyClient <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try {
            Socket socket = connectToServer(port);
            receiveFile(socket);
            socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }

    // Opens a socket to the server on the given port.
    private static Socket connectToServer(int port) throws IOException {
        System.out.println("Connecting to " + HOST + " on port " + port + "...");
        Socket socket = new Socket(HOST, port);
        System.out.println("Connected to server");
        return socket;
    }

    // Reads from the socket until the server closes the connection
    // (read() returns -1) and writes everything to client_file.txt.
    private static void receiveFile(Socket socket) throws IOException {
        System.out.println("Receiving file from server...");

        InputStream socketInput = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        int totalBytesReceived = 0;

        try (FileOutputStream fileOutput = new FileOutputStream(FILE_NAME)) {
            while ((bytesRead = socketInput.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
                totalBytesReceived += bytesRead;
            }
        }

        System.out.println("Received " + totalBytesReceived + " bytes");
        System.out.println("Saved file as " + FILE_NAME);
    }
}
