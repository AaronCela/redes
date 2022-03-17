package es.udc.redes.tutorial.copy;
import java.io.*;

public class Copy {

    public static void main(String[] args) throws IOException{
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try{
            inputStream = new BufferedReader(new FileReader(args[0]));
            outputStream = new PrintWriter(new FileWriter(args[1]));

            String a;

            while((a = inputStream.readLine()) != null){
                outputStream.println(a);

            }
        }finally{
            if(inputStream != null){
                inputStream.close();
            }

            if(outputStream != null){
                outputStream.close();
            }


        }
    }
}
