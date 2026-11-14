package com.lazar.courier.shipment.common;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}