package Clavardage.Network.Models;

import Clavardage.Utils.Serializable;

public interface PacketBasics extends Serializable {
    String getPayload();
    Address getDest();
    Address getSrc();
    void setSrc(Address src);
}
