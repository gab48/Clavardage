package Models;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class User {

    private int id;
    private InetAddress addr;
    private String nickname;

    public User() {
        try {
            this.addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            // Manually set IP address
        }
    }

    public User(InetAddress addr, String nickname) {
        this.addr = addr;
        this.nickname = nickname;
    }

    public InetAddress getAddr() {
        return addr;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
