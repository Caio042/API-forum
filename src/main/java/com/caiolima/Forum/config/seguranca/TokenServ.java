package com.caiolima.Forum.config.seguranca;

import com.caiolima.Forum.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServ {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        Date hoje = new Date();
        Date expiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API forum")  //aplicação que está gerando o token
                .setSubject(usuarioLogado.getId().toString()) // usuario / dono do token, recebe uma string que identifique unicamente o usuario
                .setIssuedAt(hoje) // quando foi gerado o token
                .setExpiration(expiracao) // quando expira
                .signWith(SignatureAlgorithm.HS256, secret) //qual a criptografia usada no token
                .compact(); //compactar e colocar numa string
    }
}
