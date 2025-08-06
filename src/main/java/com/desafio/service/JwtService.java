package com.desafio.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço para gerenciar tokens JWT.
 */
@Slf4j
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration:86400000}") // 24 horas em milissegundos
    private long jwtExpiration;
    
    /**
     * Extrai o email do token JWT.
     * 
     * @param token Token JWT
     * @return Email do usuário
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrai a data de expiração do token JWT.
     * 
     * @param token Token JWT
     * @return Data de expiração
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extrai uma claim específica do token JWT.
     * 
     * @param token Token JWT
     * @param claimsResolver Função para resolver a claim
     * @return Valor da claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extrai todas as claims do token JWT.
     * 
     * @param token Token JWT
     * @return Claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Verifica se o token JWT é válido.
     * 
     * @param token Token JWT
     * @param userDetails Detalhes do usuário
     * @return true se o token é válido, false caso contrário
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Verifica se o token JWT está expirado.
     * 
     * @param token Token JWT
     * @return true se o token está expirado, false caso contrário
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Gera um token JWT para o usuário.
     * 
     * @param userDetails Detalhes do usuário
     * @return Token JWT gerado
     */
    public String generateToken(UserDetails userDetails) {
        try {
            log.info("Gerando token para usuário: {}", userDetails.getUsername());
            return generateToken(new HashMap<>(), userDetails);
        } catch (Exception e) {
            log.error("Erro ao gerar token JWT: ", e);
            throw e;
        }
    }
    
    /**
     * Gera um token JWT para o usuário com claims extras.
     * 
     * @param extraClaims Claims extras
     * @param userDetails Detalhes do usuário
     * @return Token JWT gerado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            log.info("Construindo token JWT...");
            log.info("Username: {}", userDetails.getUsername());
            log.info("Expiration time: {} ms", jwtExpiration);
            
            String token = Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
                    
            log.info("Token JWT construído com sucesso. Tamanho: {}", token.length());
            return token;
        } catch (Exception e) {
            log.error("Erro ao construir token JWT: ", e);
            throw e;
        }
    }
    
    /**
     * Obtém a chave de assinatura para o JWT.
     * 
     * @return Chave de assinatura
     */
    private Key getSignKey() {
        try {
            log.info("Obtendo chave de assinatura JWT...");
            log.info("Secret key length: {}", secretKey.length());
            
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            log.info("Decoded key bytes length: {}", keyBytes.length);
            
            Key key = Keys.hmacShaKeyFor(keyBytes);
            log.info("Chave de assinatura criada com sucesso");
            return key;
        } catch (Exception e) {
            log.error("Erro ao criar chave de assinatura: ", e);
            throw e;
        }
    }
} 