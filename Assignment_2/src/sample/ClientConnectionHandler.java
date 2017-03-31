package sample;
//Andrew Selvarajah
//100520671

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientConnectionHandler implements Runnable {

    Socket sock;
    private BufferedReader requestInput = null;
    private DataOutputStream requestOutput = null;
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    public  ArrayList<File> files;
    public Controller controller;


    public ClientConnectionHandler(Socket sock, Controller controller) {

        this.sock = sock;
        this.controller = controller;
        try {
            requestInput = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            requestOutput = new DataOutputStream(sock.getOutputStream());
        } catch (IOException e) {
            System.err.println("Server Error while processing new socket\r\n");
            e.printStackTrace();
        }

    }
    //Takes the commands form the client
    @Override
    public void run() {

        String command = null;
        String command2 = null;

        try {
            command = requestInput.readLine();
            command2 = requestInput.readLine();
            handleRequest(command, command2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                requestInput.close();
                requestOutput.close();
                sock.close();
            } catch (IOException e2) {
            }
        }

    }
//handles the client request based on commands sent
    private void handleRequest(String mainRequestLine, String second) throws IOException {

        ObservableList<String> names = FXCollections.observableArrayList();
        int counter = 0;
        //Lists the files within the server
        if(mainRequestLine == "dir"){
            if(files.size() != 0){

                for (File file : files){
                    names.add(file.getName());
                }
                controller.getRightList().setItems(names);


            }
            else{

            }

        }
        //Sends the files from the server to client
        else if(mainRequestLine == "download"){
            File name;
            boolean check = true;


            for (File file : files) {
                if (second == file.getName()) {
                    name = file;
                    try{
                        byte [] mybytearray  = new byte [(int)name.length()];
                        fis = new FileInputStream(name);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray,0,mybytearray.length);
                        os = sock.getOutputStream();
                        System.out.println("Sending " + names + "(" + mybytearray.length + " bytes)");
                        os.write(mybytearray,0,mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
                    }
                    finally {
                        if (bis != null) bis.close();
                        if (os != null) os.close();
                        if (sock!=null) sock.close();
                    }



                }
                else {
                    System.err.println("File does not exist");
                    check = false;
                }
            }




        }
        else if(mainRequestLine == "upload"){








        }




    }
}
