package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;


public class WebServer {

    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException();

        ServerSocket sSocket;

        try {
            sSocket = new ServerSocket(Integer.parseInt(args[0]));
            while (true)
                (new ServerThread(sSocket.accept())).start();
        }
        catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}
