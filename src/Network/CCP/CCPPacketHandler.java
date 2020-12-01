package Network.CCP;

import Models.User;
import Network.Utils.Address;

public class CCPPacketHandler implements Runnable {

    private int id;
    private CCPPacket packet;

    public CCPPacketHandler(int id, CCPPacket packet) {
        super();
        this.id = id;
        this.packet = packet;
    }

    public int getId() {return this.id;}

    @Override
    public void run() {
        System.out.println("Processing this packet : " + this.packet);


        if (this.packet.getType() == 0) {
            //Get my infos
            User me = new User("XxRené.Ga56xX", new Address("127.0.0.1", (short) 1920));
            CCPController ccpController = new CCPController(me);
            ccpController.sendReplyTo(this.packet.getSrc());
        }


        //TODO: Save remoteUser
        User remoteUser = this.packet.getUserFromDiscover();
        System.out.println("Update user list with this one : \n" + remoteUser);
    }
}
