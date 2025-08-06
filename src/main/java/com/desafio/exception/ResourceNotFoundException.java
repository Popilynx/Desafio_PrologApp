package com.desafio.exception;

/**
 * Exceção lançada quando um recurso não é encontrado no sistema.
 * Utilizada para padronizar o tratamento de erros 404.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s não encontrado com %s: '%s'", resourceName, fieldName, fieldValue));
    }
} 