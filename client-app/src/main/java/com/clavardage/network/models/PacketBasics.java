package com.clavardage.network.models;

import com.clavardage.utils.Serializable;

public interface PacketBasics extends Serializable {
    String getPayload();
    Address getDest();
    Address getSrc();
    void setSrc(Address src);
}
