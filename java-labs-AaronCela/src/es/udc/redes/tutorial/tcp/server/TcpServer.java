package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String argv[]) throws IOException {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
      System.exit(-1);
    }
    ServerSocket servidor= null;
    Socket sc=null;
    try {

      // Create a server socket
      int port= Integer.parseInt(argv[0]);
      servidor= new ServerSocket(port);

      // Set a timeout of 300 secs
      servidor.setSoTimeout(300000);

      while (true) {

        // Wait for connections
        sc=servidor.accept();

        // Create a ServerThread object, with the new connection as parameter
        ServerThread sthread= new ServerThread(sc);

        // Initiate thread using the start() method
        sthread.start();
      }
      } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
	    sc.close();
    }
  }
}
