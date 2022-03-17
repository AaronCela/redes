package es.udc.redes.tutorial.udp.server;
import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }

        DatagramSocket socket = null;
        try {
            // Create a server socket
            int puerto = Integer.parseInt(argv[0]);

            socket= new DatagramSocket(puerto);

            // Set maximum timeout to 300 secs
            socket.setSoTimeout(300000);
            byte array[] = new byte[1024];

            while (true) {

                // Prepare datagram for reception
                DatagramPacket dgramRec = new DatagramPacket(array, array.length);

                // Receive the message
                socket.receive(dgramRec);

                System.out.println("SERVER: Received "
                        + new String (dgramRec.getData(),0, dgramRec.getLength())
                        + "from" + dgramRec.getAddress().toString() +":"
                        + dgramRec.getPort());

                // Prepare datagram to send response
                DatagramPacket msg = new DatagramPacket(dgramRec.getData(), dgramRec.getLength(),dgramRec.getAddress(), dgramRec.getPort());

                // Send response
                socket.send(dgramRec);
                System.out.println("SERVER: Sending "
                        + new String(msg.getData()) + " to "
                        + msg.getAddress().toString() + ":"
                        + msg.getPort());
            }

            // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
