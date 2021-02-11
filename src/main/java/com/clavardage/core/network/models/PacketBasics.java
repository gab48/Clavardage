package com.clavardage.core.network.models;

import com.clavardage.core.utils.Serializable;

public interface PacketBasics extends Serializable {
    String getPayload();
    Address getDest();
    Address getSrc();
    void setSrc(Address src);
}
