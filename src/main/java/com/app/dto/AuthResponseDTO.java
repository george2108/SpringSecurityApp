package com.app.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "accessToken", "status"})
public record AuthResponseDTO(
        String username,
        String message,
        String accessToken,
        boolean status
) {

}
