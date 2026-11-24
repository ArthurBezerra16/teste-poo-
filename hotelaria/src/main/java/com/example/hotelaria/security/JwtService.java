package com.example.hotelaria.security;

import com.example.hotelaria.model.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Gerar chave de assinatura
    private SecretKey getChave() {

        byte[] bytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(bytes);
    }

    // Gerar token JWT
    public String gerarToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
                .issuedAt(new Date())
                .expiration(
                    new Date(
                        System.currentTimeMillis() + expiration
                    )
                )
                .signWith(getChave())
                .compact();
    }

    // Extrair informações do token
    private Claims extrairClaims(String token) {

        return Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extrair e-mail
    public String extrairEmail(String token) {

        return extrairClaims(token).getSubject();
    }

    // Extrair perfil
    public String extrairPerfil(String token) {

        return extrairClaims(token)
                .get("perfil", String.class);
    }

    // Validar token
    public boolean tokenValido(String token) {

        try {

            extrairClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}