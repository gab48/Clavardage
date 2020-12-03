package Clavardage.Models;

import Clavardage.Network.Models.Address;

import java.util.Objects;

public class User {

    public static User current = null;

    private int id;
    private Address addr;
    private String nickname;

    public User(String nickname, Address addr) {
        this(nickname);
        if (addr != null) {
            this.addr = addr;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.id == user.id && Objects.equals(addr, user.addr) && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addr, nickname);
    }
}
