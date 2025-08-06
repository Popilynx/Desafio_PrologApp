package com.desafio.service;

import com.desafio.dto.PneuDTO;
import com.desafio.exception.BusinessException;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.model.Pneu;
import com.desafio.model.StatusPneu;
import com.desafio.repository.PneuRepository;
import com.desafio.repository.VeiculoPneuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações de negócio relacionadas aos pneus.
 * Implementa as regras de negócio e validações necessárias.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PneuService {
    
    private final PneuRepository pneuRepository;
    private final VeiculoPneuRepository veiculoPneuRepository;
    
    /**
     * Cria um novo pneu no sistema.
     * 
     * @param pneuDTO Dados do pneu a ser criado
     * @return PneuDTO com os dados do pneu criado
     * @throws BusinessException se o número de fogo já estiver cadastrado
     */
    public PneuDTO criarPneu(PneuDTO pneuDTO) {
        log.info("Criando novo pneu com número de fogo: {}", pneuDTO.getNumeroFogo());
        
        // Validar se número de fogo já existe
        validarNumeroFogoUnico(pneuDTO.getNumeroFogo());
        
        // Criar entidade Pneu
        Pneu pneu = criarEntidadePneu(pneuDTO);
        
        // Salvar no banco
        Pneu savedPneu = pneuRepository.save(pneu);
        log.info("Pneu criado com sucesso. ID: {}", savedPneu.getId());
        
        return converterParaDTO(savedPneu);
    }
    
    /**
     * Busca um pneu por ID.
     * 
     * @param id ID do pneu
     * @return PneuDTO com os dados do pneu
     * @throws ResourceNotFoundException se o pneu não for encontrado
     */
    @Transactional(readOnly = true)
    public PneuDTO buscarPneuPorId(Long id) {
        log.info("Buscando pneu por ID: {}", id);
        
        Pneu pneu = pneuRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pneu", "ID", id));
        
        return converterParaDTO(pneu);
    }
    
    /**
     * Busca todos os pneus cadastrados no sistema.
     * 
     * @return Lista de PneuDTO com todos os pneus
     */
    @Transactional(readOnly = true)
    public List<PneuDTO> buscarTodosPneus() {
        log.info("Buscando todos os pneus");
        
        List<Pneu> pneus = pneuRepository.findAll();
        return pneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca pneus por marca (busca parcial case-insensitive).
     * 
     * @param marca Marca a ser pesquisada
     * @return Lista de PneuDTO com os pneus encontrados
     */
    @Transactional(readOnly = true)
    public List<PneuDTO> buscarPneusPorMarca(String marca) {
        log.info("Buscando pneus por marca: {}", marca);
        
        List<Pneu> pneus = pneuRepository.findByMarcaContainingIgnoreCase(marca);
        return pneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca pneus por status.
     * 
     * @param status Status dos pneus a serem buscados
     * @return Lista de PneuDTO com os pneus encontrados
     */
    @Transactional(readOnly = true)
    public List<PneuDTO> buscarPneusPorStatus(StatusPneu status) {
        log.info("Buscando pneus por status: {}", status);
        
        List<Pneu> pneus = pneuRepository.findByStatus(status);
        return pneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca pneus disponíveis para aplicação.
     * 
     * @return Lista de PneuDTO com os pneus disponíveis
     */
    @Transactional(readOnly = true)
    public List<PneuDTO> buscarPneusDisponiveis() {
        log.info("Buscando pneus disponíveis");
        
        List<Pneu> pneus = pneuRepository.findByStatus(StatusPneu.DISPONIVEL);
        return pneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca pneus com pressão baixa (menor que 30 PSI).
     * 
     * @return Lista de PneuDTO com os pneus com pressão baixa
     */
    @Transactional(readOnly = true)
    public List<PneuDTO> buscarPneusComPressaoBaixa() {
        log.info("Buscando pneus com pressão baixa");
        
        List<Pneu> pneus = pneuRepository.findByPressaoAtualLessThan(30.0);
        return pneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza um pneu existente no sistema.
     * 
     * @param id ID do pneu a ser atualizado
     * @param pneuDTO Novos dados do pneu
     * @return PneuDTO com os dados atualizados
     * @throws ResourceNotFoundException se o pneu não for encontrado
     * @throws BusinessException se o novo número de fogo já estiver cadastrado
     */
    public PneuDTO atualizarPneu(Long id, PneuDTO pneuDTO) {
        log.info("Atualizando pneu com ID: {}", id);
        
        Pneu pneu = pneuRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pneu", "ID", id));
        
        // Verificar se número de fogo mudou e se já existe
        if (!pneu.getNumeroFogo().equals(pneuDTO.getNumeroFogo()) && 
            pneuRepository.existsByNumeroFogo(pneuDTO.getNumeroFogo())) {
            throw new BusinessException("Número de fogo já cadastrado: " + pneuDTO.getNumeroFogo());
        }
        
        // Atualizar dados
        atualizarDadosPneu(pneu, pneuDTO);
        
        // Salvar no banco
        Pneu savedPneu = pneuRepository.save(pneu);
        log.info("Pneu atualizado com sucesso. ID: {}", savedPneu.getId());
        
        return converterParaDTO(savedPneu);
    }
    
    /**
     * Remove um pneu do sistema.
     * 
     * @param id ID do pneu a ser removido
     * @throws ResourceNotFoundException se o pneu não for encontrado
     * @throws BusinessException se o pneu estiver aplicado em um veículo
     */
    public void deletarPneu(Long id) {
        log.info("Deletando pneu com ID: {}", id);
        
        if (!pneuRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pneu", "ID", id);
        }
        
        // Verificar se está aplicado em algum veículo
        if (veiculoPneuRepository.existsByPneuId(id)) {
            throw new BusinessException("Não é possível deletar pneu que está aplicado em um veículo");
        }
        
        pneuRepository.deleteById(id);
        log.info("Pneu deletado com sucesso. ID: {}", id);
    }
    
    /**
     * Busca um pneu por número de fogo.
     * 
     * @param numeroFogo Número de fogo do pneu
     * @return PneuDTO com os dados do pneu
     * @throws ResourceNotFoundException se o pneu não for encontrado
     */
    @Transactional(readOnly = true)
    public PneuDTO buscarPneuPorNumeroFogo(String numeroFogo) {
        log.info("Buscando pneu por número de fogo: {}", numeroFogo);
        
        Pneu pneu = pneuRepository.findByNumeroFogo(numeroFogo)
            .orElseThrow(() -> new ResourceNotFoundException("Pneu", "número de fogo", numeroFogo));
        
        return converterParaDTO(pneu);
    }
    
    /**
     * Atualiza a pressão de um pneu específico.
     * 
     * @param id ID do pneu
     * @param novaPressao Nova pressão a ser definida
     * @return PneuDTO com os dados atualizados
     * @throws ResourceNotFoundException se o pneu não for encontrado
     */
    public PneuDTO atualizarPressaoPneu(Long id, Double novaPressao) {
        log.info("Atualizando pressão do pneu com ID: {} para {} PSI", id, novaPressao);
        
        Pneu pneu = pneuRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pneu", "ID", id));
        
        pneu.setPressaoAtual(novaPressao);
        
        Pneu savedPneu = pneuRepository.save(pneu);
        log.info("Pressão do pneu atualizada com sucesso. ID: {}", savedPneu.getId());
        
        return converterParaDTO(savedPneu);
    }
    
    /**
     * Valida se o número de fogo é único no sistema.
     * 
     * @param numeroFogo Número de fogo a ser validado
     * @throws BusinessException se o número de fogo já estiver cadastrado
     */
    private void validarNumeroFogoUnico(String numeroFogo) {
        if (pneuRepository.existsByNumeroFogo(numeroFogo)) {
            throw new BusinessException("Número de fogo já cadastrado: " + numeroFogo);
        }
    }
    
    /**
     * Cria uma entidade Pneu a partir de um DTO.
     * 
     * @param pneuDTO DTO com os dados do pneu
     * @return Entidade Pneu criada
     */
    private Pneu criarEntidadePneu(PneuDTO pneuDTO) {
        Pneu pneu = new Pneu();
        pneu.setNumeroFogo(pneuDTO.getNumeroFogo());
        pneu.setMarca(pneuDTO.getMarca());
        pneu.setPressaoAtual(pneuDTO.getPressaoAtual());
        pneu.setStatus(pneuDTO.getStatus() != null ? pneuDTO.getStatus() : StatusPneu.DISPONIVEL);
        return pneu;
    }
    
    /**
     * Atualiza os dados de um pneu existente.
     * 
     * @param pneu Entidade a ser atualizada
     * @param pneuDTO Dados para atualização
     */
    private void atualizarDadosPneu(Pneu pneu, PneuDTO pneuDTO) {
        pneu.setNumeroFogo(pneuDTO.getNumeroFogo());
        pneu.setMarca(pneuDTO.getMarca());
        pneu.setPressaoAtual(pneuDTO.getPressaoAtual());
        if (pneuDTO.getStatus() != null) {
            pneu.setStatus(pneuDTO.getStatus());
        }
    }
    
    /**
     * Converte uma entidade Pneu para DTO.
     * 
     * @param pneu Entidade a ser convertida
     * @return PneuDTO com os dados convertidos
     */
    private PneuDTO converterParaDTO(Pneu pneu) {
        return new PneuDTO(
            pneu.getId(),
            pneu.getNumeroFogo(),
            pneu.getMarca(),
            pneu.getPressaoAtual(),
            pneu.getStatus(),
            pneu.getCreatedAt(),
            pneu.getUpdatedAt()
        );
    }
} 