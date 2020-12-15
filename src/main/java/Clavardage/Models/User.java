package Clavardage.Models;

import Clavardage.Network.Models.Address;

import java.util.Objects;

public class User {

    private static boolean isLocalUserInstantiated = false;

    public static User localUser;

    public static void instantiateLocalUser(String nickname) {
        if (isLocalUserInstantiated) {
            throw new IllegalStateException("localUser already instantiated");
        } else {
            User.localUser = new User(nickname, Address.getMyIP());
            User.isLocalUserInstantiated = true;
        }
    }

    protected int id;
    protected Address address;
    protected String nickname;

    public User(String nickname, Address address) {
        this.nickname = nickname;
        this.address = address;
    }

    public User(Address addr) {
        this(null, addr);
    }

    public Address getAddress() {
        return address;
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
                ", address=" + address +
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
        if (this.getClass() == o.getClass()) {
            User user = (User) o;
            return Objects.equals(this.id, user.id) && Objects.equals(this.address, user.address) && Objects.equals(this.nickname, user.nickname);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, nickname);
    }

    public boolean correspondTo(User u) {
        return this.address.toString().equals(u.address.toString());
    }
}
