package Clavardage.Models;

import Clavardage.Network.Models.Address;

public class LocalUser extends User{

    public static volatile LocalUser INSTANCE = null;

    private LocalUser(String nickname) {
        super(nickname, Address.getMyIP());
    }

    public static LocalUser instanciate(String nickname) {
        if(INSTANCE == null) {
            synchronized (LocalUser.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalUser(nickname);
                }
            }
        }
        return INSTANCE;
    }

    public static LocalUser getInstance() {
        return INSTANCE;
    }
}
