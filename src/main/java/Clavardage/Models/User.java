package Clavardage.Models;

import Clavardage.Network.Models.Address;

import java.util.Objects;

public class User {

    protected int id;
    protected Address addr;
    protected String nickname;

    public User(String nickname, Address addr) {
        this.nickname = nickname;
        this.addr = addr;
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

    public String debugString() {
        return "User{" +
                "id=" + id +
                ", addr=" + addr +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return this.nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) {
            User user = (User) o;
            return Objects.equals(this.id, user.id) && Objects.equals(this.addr, user.addr) && Objects.equals(this.nickname, user.nickname);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addr, nickname);
    }
}
