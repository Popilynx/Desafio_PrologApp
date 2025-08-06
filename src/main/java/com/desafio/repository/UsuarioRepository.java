package com.desafio.repository;

import com.desafio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações com usuários.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário
     * @return Optional com o usuário encontrado
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica se existe um usuário com o email informado.
     * 
     * @param email Email a ser verificado
     * @return true se o email já existe, false caso contrário
     */
    boolean existsByEmail(String email);
} 