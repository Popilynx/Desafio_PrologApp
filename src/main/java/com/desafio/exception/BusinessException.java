package com.desafio.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 * Utilizada para padronizar o tratamento de erros de validação.
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 