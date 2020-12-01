package Models;

import Network.Utils.Address;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class User {

    private int id;
    private Address addr;
    private String nickname;

    public User(String nickname, Address addr) {
        this.nickname = nickname;
        this.addr = addr;
    }

    public User(String nickname) {
        this.nickname = nickname;
        this.addr = Address.getMyIP();
    }

    public Address getAddr() {
        return addr;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", addr=" + addr +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
