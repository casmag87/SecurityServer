package model;

import java.net.Socket;
import org.mindrot.jbcrypt.BCrypt;
public class User {

    private String name;
    private String password;
    private Socket socket;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = BCrypt.hashpw(password,BCrypt.gensalt());
    }

    public boolean verifyPassword(String userPassword){
        return(BCrypt.checkpw(userPassword,password));
    }

    public User(String name, String password, Socket socket) {
        this.name = name;
        this.password = BCrypt.hashpw(password,BCrypt.gensalt());
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", socket=" + socket +
                '}';
    }
}
