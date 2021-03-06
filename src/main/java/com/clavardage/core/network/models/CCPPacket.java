package com.clavardage.core.network.models;

import com.clavardage.core.models.User;
import com.clavardage.core.network.types.CCPPacketType;
import com.clavardage.core.utils.Config;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CCPPacket extends Packet {

    private int type; // 0=Discovery; 1=Reply; 2=Disconnect

    private void setMulticastDestination() {
        this.dest = Address.getMulticast();
        if (this.dest != null) {
            this.dest.setPort(Config.getShort("NETWORK_CLAVARDAGE_PORT"));
        }
    }

    public CCPPacket(CCPPacketType type, User user) {
        super();
        if (type == CCPPacketType.DISCOVER) {
            this.type = 0;
            this.setMulticastDestination();
        } else if (type == CCPPacketType.REPLY) {
            this.type = 1;
        } else if (type == CCPPacketType.DISCONNECT){
            this.type = 2;
            this.setMulticastDestination();
        }
        this.payload = user.getNickname()+'='+this.src.getIp().getHostAddress();
    }

    public CCPPacket() { super(); }

    public int getType() { return type; }
    public void setDestAddr (Address addr) {
        this.dest = addr;
        this.dest.setPort(Config.getShort("NETWORK_CLAVARDAGE_PORT"));
    }

    @Override
    public String toString() {
        return "CCP "+this.type+" ["+this.payload+']';
    }

    @Override
    public byte[] serialize() {
        return this.toString().getBytes();
    }

    @Override
    public void unserialize(byte[] bytes) {
        String string = new String(bytes);
        String[] result = string.split(" ");
        if(result.length != 3) {
            System.out.println("Format incorrect");
            System.exit(1);
        }
        this.type = Integer.parseInt(result[1]);
        if(result[2].trim().matches("\\[(.*)]")){
            this.payload = result[2].split("]")[0].substring(1);
        }
    }

    public User getUserFromCCP() {
        User remote = null;
        String[] infos = this.payload.split("=");
        if(infos.length != 2) {
            System.out.println("Format incorrect");
            System.exit(1);
        }
        try {
            remote = new User(infos[0], new Address(InetAddress.getByName(infos[1])));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return remote;
    }
}
