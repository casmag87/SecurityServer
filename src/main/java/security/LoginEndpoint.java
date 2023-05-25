package security;
import client.ClientHandler;
import model.User;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LoginEndpoint {


    private Map<String,String> users;
    private Map<String,ClientHandler> activeSessions;

    User user;

    public LoginEndpoint(){
        users = new HashMap<>();
        activeSessions = new HashMap<>();
    }

    public void addSession(String name, ClientHandler clientHandler) {
        activeSessions.put(name, clientHandler);
    }

    public void removeSession(String name, ClientHandler clientHandler){activeSessions.remove(name,clientHandler);}

    public LoginEndpoint(Map<String, ClientHandler> activeSessions) {
        this.activeSessions = activeSessions;
    }

    public void registerUsers(String username, String password){
        users.put(username,password);
    }

    public boolean loginUser(String username, String password){
        if(users.containsKey(username)){
            String storedPassword = users.get(username);
            if (storedPassword.equals(password)){
                return true;
            }
        }
        return false;


    }

    @Override
    public String toString() {
        return "loginEndpoint{" +
                "users=" + users +
                ", activeSessions=" + activeSessions +
                '}';
    }
}
