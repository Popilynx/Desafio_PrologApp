package com.desafio.service;

import com.desafio.dto.AuthResponseDTO;
import com.desafio.dto.LoginRequestDTO;
import com.desafio.dto.RegistroRequestDTO;
import com.desafio.exception.BusinessException;
import com.desafio.model.Usuario;
import com.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço para autenticação e registro de usuários.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    /**
     * Registra um novo usuário no sistema.
     * 
     * @param request Dados do usuário para registro
     * @return Resposta com token JWT
     */
    public AuthResponseDTO registrar(RegistroRequestDTO request) {
        try {
            log.info("Tentando registrar usuário com email: {}", request.getEmail());
            
            // Verificar se o email já existe
            log.info("Verificando se email já existe...");
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                log.warn("Email já existe: {}", request.getEmail());
                throw new BusinessException("Email já está cadastrado no sistema");
            }
            
            // Criar novo usuário
            log.info("Criando novo usuário...");
            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            
            log.info("Codificando senha...");
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
            usuario.setRole(Usuario.Role.USER);
            
            // Salvar no banco
            log.info("Salvando usuário no banco...");
            Usuario savedUsuario = usuarioRepository.save(usuario);
            log.info("Usuário registrado com sucesso. ID: {}", savedUsuario.getId());
            
            // Gerar token JWT
            log.info("Gerando token JWT...");
            String token = jwtService.generateToken(savedUsuario);
            log.info("Token JWT gerado com sucesso");
            
            return new AuthResponseDTO(token, savedUsuario.getEmail(), savedUsuario.getNome(), savedUsuario.getRole().name());
        } catch (Exception e) {
            log.error("Erro ao registrar usuário: ", e);
            throw e;
        }
    }
    
    /**
     * Autentica um usuário existente.
     * 
     * @param request Dados de login
     * @return Resposta com token JWT
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        log.info("Tentando autenticar usuário com email: {}", request.getEmail());
        
        // Autenticar com Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        
        // Buscar usuário
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        
        // Gerar token JWT
        String token = jwtService.generateToken(usuario);
        
        log.info("Usuário autenticado com sucesso. ID: {}", usuario.getId());
        
        return new AuthResponseDTO(token, usuario.getEmail(), usuario.getNome(), usuario.getRole().name());
    }
} 