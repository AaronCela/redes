package es.udc.redes.webserver;
import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Request {
    private final String[] lineRequest;
    private int status=200;
    private File file;
    private PrintWriter outputPw;
    private OutputStream outputS;



    public Request(String[] request, OutputStream output) {
        this.lineRequest = request;
        this.outputS= output;
    }

    public void Response(){
        String FileName;

        FileName=validate();
        outputPw = new PrintWriter(outputS, true);
        if(FileName == null) {
            file = new File("p1-files", "error400.html");
            status = 400;
        }

        else{
            file = new File("p1-files",FileName);
        }

        if(!file.exists()) {
            file = new File("p1-files", "error404.html");
            status = 404;
        }
        IfModifiedSince();
        Head(outputPw, status);
    }


    public String validate(){
        String line = lineRequest[0];
        String[] chunks= line.split(" ");
        String str;


        if(chunks.length <3)
            return null;

        if (!chunks[2].startsWith("HTTP/"))
            return null;

        str= chunks[0];
        if(!str.equals("GET") && !str.equals("HEAD"))
            return null;

        return chunks[1];
    }

    public boolean modVerificate(File f, String date) {
        long segundosFecha = (ZonedDateTime.parse(date,(DateTimeFormatter.RFC_1123_DATE_TIME)).toEpochSecond());

        long f_fecha = f.lastModified()/1000;

        return (segundosFecha < f_fecha);
    }

    public void IfModifiedSince() {
        for (String request : lineRequest) {
            if (request.startsWith("If-Modified-Since: ")) {
                if (!modVerificate(file, request.substring(19))) {
                    status = 304;
                    return;
                }
            }
        }
    }


    public static String getDate() {
        return "Date: " +
                ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                System.lineSeparator();
    }

    public void typeContent(File file,PrintWriter outputPw){
        String typeContent;

        typeContent= file.getPath().substring(file.getPath().lastIndexOf(".") + 1);

        if(typeContent.equals("txt"))
            typeContent="plain";

        if(typeContent.equals("html") || typeContent.equals("plain"))
            outputPw.println("Content-Type: text/" + typeContent);

        else if (typeContent.equals("gif") || typeContent.equals("png")
                || typeContent.equals("jpg")) {
            outputPw.println("Content-Type: image/" + typeContent);
        }

        else
            outputPw.println("Content-Type: application/octet-stream");

    }

    public String lastModifiedStr(){
        return "Last-Modified: " + DateTimeFormatter.RFC_1123_DATE_TIME.format(
                ZonedDateTime.ofInstant(
                        Instant.ofEpochMilli(file.lastModified())
                        , ZoneOffset.UTC
                )
        );
    }

    public void responseCode(PrintStream output) {
        FileInputStream input;
        int c;

        try {
            input = new FileInputStream(file);
            while ((c = input.read()) != -1)
                output.write(c);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Head(PrintWriter output, int status){

        if(status == 200)
            outputPw.println("HTTP/1.0 200 OK");

        if(status == 304)
            outputPw.println("HTTP/1.0 304 NOT MODIFIED");

        if(status == 400)
            outputPw.println("HTTP/1.0 400 BAD REQUEST");

        if(status == 404)
            outputPw.println("HTTP/1.0 404 NOT FOUND");

        outputPw.write(getDate());

        outputPw.println("Server: WebServer");

        if (status == 304) return;

        outputPw.println(lastModifiedStr());

        typeContent(file,outputPw);


        try {
            outputPw.println("Content-Length: " + Files.size(file.toPath()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        output.println("");

        if((lineRequest[0].split(" ")[0]).equals("GET") || status==400 )
            responseCode(new PrintStream(outputS,true));
    }
}
