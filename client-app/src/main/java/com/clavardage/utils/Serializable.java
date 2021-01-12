package com.clavardage.utils;

public interface Serializable {

    String SEPARATOR = ",";

    byte[] serialize();
    void unserialize(byte[] bytes);
}