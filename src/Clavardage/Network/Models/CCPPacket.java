package Clavardage.Network.Models;

import Clavardage.Env;
import Clavardage.Models.User;
import Clavardage.Network.Listeners.CCPListenerPool;
import Clavardage.Network.Types.CCPPacketType;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CCPPacket extends Packet {

    private int type;

    public CCPPacket(CCPPacketType type, User user) {
        super();
        if (type == CCPPacketType.DISCOVER) {
            this.type = 0;
            this.dest = Address.getBroadcast();
            if (this.dest != null) {
                this.dest.setPort(Env.NETWORK_UDP_SRV_PORT);
            }
            this.payload = user.getNickname()+'='+this.src.getIp().getHostAddress();
        }
        if (type == CCPPacketType.REPLY) {
            this.type = 1;
            this.payload = user.getNickname()+'='+this.src.getIp().getHostAddress();
        }
    }

    public CCPPacket() { super(); }
    public CCPPacket(byte[] bytes) { super(bytes); }

    public int getType() { return type; }
    public void setDestAddr (Address addr) {
        this.dest = addr;
        this.dest.setPort(Env.NETWORK_UDP_SRV_PORT);
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

    public User getUserFromDiscover() {
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
