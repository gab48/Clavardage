package com.clavardage.core.utils;

public interface Serializable {

    String SEPARATOR = ",";

    byte[] serialize();
    void unserialize(byte[] bytes);
}