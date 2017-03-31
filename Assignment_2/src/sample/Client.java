package sample;
//Andrew Selvarajah
//100520671
import javafx.collections.FXCollections;


import javafx.collections.ObservableList;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Client {

    String host;
    int port;
    Socket sock;
    Controller controller = new Controller();



    public Client(String host, int port){

        this.host = host;
        this.port = port;




    }//gets the names of the client files
    public void getNames(){
        ArrayList<String> names = new ArrayList<>();
        int counter = 0;
        String response;
        BufferedReader in;
        PrintWriter out;

        try{
            sock = new Socket(this.host, this.port);

            out = new PrintWriter(sock.getOutputStream());

            String request = "dir";
            out.print(request);
            out.print(" ");
            out.flush();
            sock.close();

        }catch (IOException e) {
            e.printStackTrace();
        }



    }
//recieves the files from the server
    public void recieveFiles(String pathname){

        File[] files;
        int bytesRead;
        int current;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        String response;
        BufferedReader in;
        PrintWriter out;
        ObservableList<String> namesClient;





        try{
            sock = new Socket(this.host, this.port);



            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());

            String request = "download";
            String request2 = pathname;
            out.print(request);
            out.print(request2);
            out.flush();

            //recieves the file through a byte array
            byte [] mybytearray  = new byte [100000];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(pathname);
            bos = new BufferedOutputStream(fos);

            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);
            //writes the file from the bufferedoutputStream
            bos.write(mybytearray, 0 , current);
            bos.flush();

            sock.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
        //refreshed Listview
        namesClient = FXCollections.observableArrayList();

        File file = new File("src/Files");

        File[] filesInDir = file.listFiles();


        for (int x = 0; x < filesInDir.length; x++){
            namesClient.add(filesInDir[x].getName());
        }

        controller.getLeftList().setItems(namesClient);

    }

//sends the client files to server
    public void sendFiles(String filename){
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket sock = null;
        String response;
        BufferedReader in;
        PrintWriter out;

        try {
            sock = new Socket(this.host, this.port);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());

            String request = "upload";
            String request2 = filename;
            out.print(request);
            out.print(request2);
            out.flush();
            //modifies file to byte array
            File myFile = new File (filename);
            byte [] mybytearray  = new byte [(int)myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            //sends the byte array
            os = sock.getOutputStream();
            System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();

            sock.close();



        }catch (IOException e) {
            e.printStackTrace();
        }




    }
}
