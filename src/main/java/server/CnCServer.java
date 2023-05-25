package server;

import client.ClientHandler;
import model.User;
import security.LoginEndpoint;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CnCServer {

    private ServerSocket serverSocket;
    private boolean running;

    LoginEndpoint loginEndpoint = new LoginEndpoint();

    public CnCServer(int port){
        try{

            serverSocket = new ServerSocket(port);
            System.out.println("C&C Server is running on port " + port);

        } catch (IOException e) {
            System.out.println("Failed to start the C&C Server on port " + port);
            e.printStackTrace();
        }
    }

    public void start(){
        LoginEndpoint loginEndpoint = new LoginEndpoint();
        User user = new User("nibro","0704");
        loginEndpoint.registerUsers(user.getName(), user.getPassword());
        System.out.println(loginEndpoint);

        running = true;
        String ip = "localhost";

        while(running){
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Read the command from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String command = in.readLine();
                System.out.println("Received command: " + command);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Enter password: ");
                out.flush();

                // Read the password from the client
                String password = in.readLine();

                // Validate the password
                boolean isAuthenticated = user.verifyPassword(password);

                if(isAuthenticated) {
                    // Create a new thread to handle the client's requests
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    loginEndpoint.addSession(user.getName(), clientHandler);
                    System.out.println(loginEndpoint);
                    Thread thread = new Thread(clientHandler);

                    thread.start();

                }else{
                    out.println("Invalid password. Connection closed.");
                    out.flush();
                    clientSocket.close();
                }



        } catch (IOException e){
                System.out.println("Error accepting client connection");
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        running = false;

        try{
            serverSocket.close();
            System.out.println("C&C Server stopped");
        }catch (IOException e){
            System.out.println("Error stopping the C&C Server");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        CnCServer cnCServer = new CnCServer(443);

        System.out.println();
        cnCServer.start();

    }

}
