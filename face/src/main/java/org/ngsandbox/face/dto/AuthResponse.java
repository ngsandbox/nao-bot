package org.ngsandbox.face.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private AuthDtoResponse dto;
    private String errorDescription;
    private String status;
}
