package com.caiolima.Forum.controller.dto;

import lombok.Getter;

@Getter
public class TokenDTO {

    private final String token;
    private final String tipo;

    public TokenDTO(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }
}
