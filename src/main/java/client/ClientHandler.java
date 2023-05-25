package client;

import server.CnCServer;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final OutputStream outputStream;
    private InputStream inputStream;

    CnCServer cnCServer;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.outputStream = clientSocket.getOutputStream();
        this.inputStream = clientSocket.getInputStream();
    }

    @Override
    public void run() {
        try {
            handleClient(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleClient(Socket clientSocket) throws IOException, InterruptedException {
        outputStream.write("Welcome, type a command or send 'quit' to exit\n".getBytes());

        while (true) {


            String receivedData = reader.readLine();

            if (receivedData != null && receivedData.length() <= 10) {
                String response = processRequest(receivedData);
                sendResponse(response);
                System.out.println("Response from server: " + response);

                if (receivedData.equalsIgnoreCase("quit")) {
                    break; // Exit the loop if the client sends "quit" command
                }
            } else {
                System.err.println("Invalid request: Request length exceeds the maximum allowed length.");
            }
        }

        // Clean up resources
        reader.close();
        writer.close();
        clientSocket.close();
    }






    private String processRequest(String request) throws InterruptedException {
        String trimmedRequest = request.trim(); // Trim leading and trailing whitespace

        String response;

        switch (trimmedRequest) {
            case "info":

                response = "C&C Server running.";
                System.out.println(response);
                break;
            case "time":
                response = getCurrentTime();
                System.out.println(response);
                break;
            case "quit":
                response = "Exiting C&C Server.";
                System.out.println(response);


                break;
            default:
                if (AllowedCommand.isCommandAllowed(trimmedRequest)) {
                    String command = trimmedRequest.substring(7).trim(); // Extract the command from the request and trim
                    response = executeCommand(command);
                    System.out.println(response);
                } else {
                    response = "Unknown command.";
                    System.out.println(response);
                }
                break;
        }

        return response;
    }

    private String executeCommand(String command) {
        // Code to execute a PowerShell command provided by the client
        // ...
        return "Command executed: " + command;
    }

    private String getCurrentTime() {
        // Get the current time and return it as a response
        String currentTime = LocalDateTime.now().toString();
        return "Current time is: " + currentTime;
    }

    private void sendResponse(String response) throws IOException {
        if (clientSocket.isConnected()) {
            outputStream.write(response.getBytes());
            outputStream.write(System.lineSeparator().getBytes());
            outputStream.flush();
        } else {
            System.out.println("Client disconnected before sending the response.");
        }
    }

    public void sendCommand(String command) throws IOException {
        sendRequest(command + "\n");

        String response = reader.readLine();
        System.out.println("Response from server: " + response);
    }

    private void sendRequest(String request) throws IOException {
        writer.write(request);
        writer.newLine();
        writer.flush();
    }


}