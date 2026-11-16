package com.lazar.courier.shipment.security;

public record AuthenticatedUser(Long userId, String email, String role) {

}
