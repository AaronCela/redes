package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);

        }
        ServerSocket server= null;
        Socket socket = null;

        try {
            // Create a server socket
            int puerto= Integer.parseInt(argv[0]);
            server= new ServerSocket(puerto);

            // Set a timeout of 300 secs
            server.setSoTimeout(300000);
            
            while (true) {
                // Wait for connections
                socket=server.accept();
                // Set the input channel
                BufferedReader sInput = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                // Set the output channel
                PrintWriter sOutput = new PrintWriter(socket.getOutputStream(), true);


                // Receive the client message
                String message= sInput.readLine();

                System.out.println("SERVER: Received " + message +
                        " from " + socket.getInetAddress().toString() + socket.getPort());
                // Send response to the client
                sOutput.write(message);
                System.out.println("SERVER: Sending " + message +
                        " to " + socket.getInetAddress().toString() + socket.getPort());

                // Close the streams
                sOutput.close();
                sInput.close();
            }
        } catch (SocketTimeoutException e) {
                System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        server.close();
        }
    }
}
