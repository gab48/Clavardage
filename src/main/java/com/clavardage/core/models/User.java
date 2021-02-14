package com.clavardage.core.models;

import com.clavardage.core.database.queries.inserts.ParticipantInsertQuery;
import com.clavardage.core.database.queries.QueryParameters;
import com.clavardage.core.network.controllers.CCPController;
import com.clavardage.core.network.http.StatusUpdateRequest;
import com.clavardage.core.network.models.Address;
import com.clavardage.core.utils.Config;

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

    public static void disconnectLocalUser() {
        if (isLocalUserInstantiated) {
            new CCPController().sendDisconnect();
        } else {
            throw new IllegalStateException("Disconnect: localUser needs to be instantiated");
        }
    }

    public static void connectLocalUser() {
        if (isLocalUserInstantiated) {
            new CCPController().sendDiscovery();
        } else {
            throw new IllegalStateException("Connect: localUser needs to be instantiated");
        }
    }

    public enum UserStatus {CONNECTED, IDLE, DISCONNECTED, UNKNOWN}

    public static UserStatus IntToUserStatus(int status) {
        UserStatus userStatus;
        switch (status) {
            case 1: userStatus = UserStatus.CONNECTED; break;
            case 2: userStatus = UserStatus.IDLE; break;
            case 3: userStatus = UserStatus.DISCONNECTED; break;
            default: userStatus = UserStatus.UNKNOWN; break;
        }
        return userStatus;
    }

    public static void updateLocalUserStatus(UserStatus status) {
        if (isLocalUserInstantiated) {
            localUser.setStatus(status);

            StatusUpdateRequest request = new StatusUpdateRequest(Config.get("SERVLET_ADDR"), localUser.address.toString(), localUser.status);
            request.executePost();
        }
    }

    protected final Address address;
    protected String nickname;
    protected int status; // 1=Connected; 2=Idle; 3=Disconnected; 4=UNKNOWN

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
    public UserStatus getStatus() { return IntToUserStatus(this.status); }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStatus(UserStatus status) {
        switch (status) {
            case CONNECTED: this.status = 1; break;
            case IDLE: this.status = 2; break;
            case DISCONNECTED: this.status = 3; break;
            default: this.status = 4; break;
        }
    }

    public void join(Conversation conv) {
        if (conv.getId() != -1) {
            ParticipantInsertQuery query = new ParticipantInsertQuery();
            QueryParameters parameters = new QueryParameters();

            parameters.append(conv.getId());
            parameters.append(this.getAddress().toString());
            query.prepare();
            query.setParameters(parameters);
            query.execute();
            query.close();
        }
    }

    public String debugString() {
        return "User{" +
                "address=" + address +
                ", nickname='" + nickname + '\'' +
                ", status="+ status +
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
        if (this.getClass() != o.getClass()) return false;

        User user = (User) o;
        return  Objects.equals(this.address, user.address)
                && Objects.equals(this.nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, nickname, status);
    }

    public boolean correspondTo(User u) {
        return this.address.toString().equals(u.address.toString());
    }
}
