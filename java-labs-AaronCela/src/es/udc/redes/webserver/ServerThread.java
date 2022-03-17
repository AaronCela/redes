package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.Buffer;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        String readRequest;
        StringBuilder request= new StringBuilder();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream();

            do{
                readRequest=input.readLine();

                if(readRequest==null)
                    return;

                request.append(readRequest);

                request.append(System.lineSeparator());
                System.out.println("request = " + request.toString());

            }while(!readRequest.equals(""));

            Request HTTP = new Request(request.toString().split(System.lineSeparator()), output);
            HTTP.Response();


            input.close();
            output.close();
        } catch (SocketTimeoutException e) {
           System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the client socket
        }
    }
}
