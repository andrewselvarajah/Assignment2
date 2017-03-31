package sample;

import javax.sound.sampled.Port;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//Opens a server to for a client to connect to
public class Server {

    private FileInputStream fis;
    private BufferedInputStream bis;
    private OutputStream os;
    private static ArrayList<Socket> connectionArray;
    private static Controller controller;
    private ServerSocket serverSocket = null;



    public Controller getController(){
        return controller;
    }




    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            handleRequests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Handles client requestes to connect to server
    public void handleRequests() {
        System.out.println("Server Listening");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();//wait for connection
                //calls ClientConnectionHandler class to talk with clients
                ClientConnectionHandler clientHandler=new ClientConnectionHandler(clientSocket, controller);
                Thread clientThread=new Thread(clientHandler);
                clientThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.handleRequests();
    }

}
