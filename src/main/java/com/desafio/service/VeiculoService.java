package com.desafio.service;

import com.desafio.dto.VeiculoDTO;
import com.desafio.dto.VeiculoPneuDTO;
import com.desafio.exception.BusinessException;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.model.Veiculo;
import com.desafio.model.VeiculoPneu;
import com.desafio.repository.VeiculoRepository;
import com.desafio.repository.VeiculoPneuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar veículos.
 * Aqui ficam as regras de negócio e validações.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VeiculoService {
    
    private final VeiculoRepository veiculoRepository;
    private final VeiculoPneuRepository veiculoPneuRepository;
    
    /**
     * Cria um veículo novo.
     * 
     * @param veiculoDTO Dados do veículo
     * @return Veículo criado
     * @throws BusinessException se a placa já existe
     */
    public VeiculoDTO criarVeiculo(VeiculoDTO veiculoDTO) {
        log.info("Criando veículo placa: {}", veiculoDTO.getPlaca());
        
        // Verificar se placa já existe
        validarPlacaUnica(veiculoDTO.getPlaca());
        
        // Criar veículo
        Veiculo veiculo = criarEntidadeVeiculo(veiculoDTO);
        
        // Salvar no banco
        Veiculo savedVeiculo = veiculoRepository.save(veiculo);
        log.info("Veículo criado! ID: {}", savedVeiculo.getId());
        
        return converterParaDTO(savedVeiculo);
    }
    
    /**
     * Busca um veículo pelo ID.
     * 
     * @param id ID do veículo
     * @return Veículo encontrado
     * @throws ResourceNotFoundException se não encontrar
     */
    @Transactional(readOnly = true)
    public VeiculoDTO buscarVeiculoPorId(Long id) {
        log.info("Buscando veículo ID: {}", id);
        
        Veiculo veiculo = veiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", "ID", id));
        
        return converterParaDTO(veiculo);
    }
    
    /**
     * Busca um veículo com seus pneus.
     * 
     * @param id ID do veículo
     * @return Veículo com pneus
     * @throws ResourceNotFoundException se não encontrar
     */
    @Transactional(readOnly = true)
    public VeiculoDTO buscarVeiculoComPneusPorId(Long id) {
        log.info("Buscando veículo com pneus ID: {}", id);
        
        Veiculo veiculo = veiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", "ID", id));
        
        VeiculoDTO veiculoDTO = converterParaDTO(veiculo);
        
        // Buscar pneus do veículo
        List<VeiculoPneu> veiculoPneus = veiculoPneuRepository.findByVeiculoId(id);
        List<VeiculoPneuDTO> pneusAplicados = veiculoPneus.stream()
                .map(this::converterVeiculoPneuParaDTO)
                .collect(Collectors.toList());
        
        veiculoDTO.setPneusAplicados(pneusAplicados);
        return veiculoDTO;
    }
    
    /**
     * Lista todos os veículos.
     * 
     * @return Lista de VeiculoDTO com todos os veículos
     */
    @Transactional(readOnly = true)
    public List<VeiculoDTO> buscarTodosVeiculos() {
        log.info("Buscando todos os veículos");
        
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return veiculos.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca veículos por marca (busca parcial case-insensitive).
     * 
     * @param marca Marca a ser pesquisada
     * @return Lista de VeiculoDTO com os veículos encontrados
     */
    @Transactional(readOnly = true)
    public List<VeiculoDTO> buscarVeiculosPorMarca(String marca) {
        log.info("Buscando veículos por marca: {}", marca);
        
        List<Veiculo> veiculos = veiculoRepository.findByMarcaContainingIgnoreCase(marca);
        return veiculos.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca veículos por status.
     * 
     * @param status Status dos veículos a serem buscados
     * @return Lista de VeiculoDTO com os veículos encontrados
     */
    @Transactional(readOnly = true)
    public List<VeiculoDTO> buscarVeiculosPorStatus(com.desafio.model.StatusVeiculo status) {
        log.info("Buscando veículos por status: {}", status);
        
        List<Veiculo> veiculos = veiculoRepository.findByStatus(status);
        return veiculos.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza um veículo existente no sistema.
     * 
     * @param id ID do veículo a ser atualizado
     * @param veiculoDTO Novos dados do veículo
     * @return VeiculoDTO com os dados atualizados
     * @throws ResourceNotFoundException se o veículo não for encontrado
     * @throws BusinessException se a nova placa já estiver cadastrada
     */
    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) {
        log.info("Atualizando veículo com ID: {}", id);
        
        Veiculo veiculo = veiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", "ID", id));
        
        // Verificar se placa mudou e se já existe
        if (!veiculo.getPlaca().equals(veiculoDTO.getPlaca()) && 
            veiculoRepository.existsByPlaca(veiculoDTO.getPlaca())) {
            throw new BusinessException("Placa já cadastrada: " + veiculoDTO.getPlaca());
        }
        
        // Atualizar dados
        atualizarDadosVeiculo(veiculo, veiculoDTO);
        
        // Salvar no banco
        Veiculo savedVeiculo = veiculoRepository.save(veiculo);
        log.info("Veículo atualizado com sucesso. ID: {}", savedVeiculo.getId());
        
        return converterParaDTO(savedVeiculo);
    }
    
    /**
     * Remove um veículo do sistema.
     * 
     * @param id ID do veículo a ser removido
     * @throws ResourceNotFoundException se o veículo não for encontrado
     * @throws BusinessException se o veículo tiver pneus aplicados
     */
    public void deletarVeiculo(Long id) {
        log.info("Deletando veículo com ID: {}", id);
        
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo", "ID", id);
        }
        
        // Verificar se tem pneus aplicados
        List<VeiculoPneu> veiculoPneus = veiculoPneuRepository.findByVeiculoId(id);
        if (!veiculoPneus.isEmpty()) {
            throw new BusinessException("Não é possível deletar veículo com pneus aplicados");
        }
        
        veiculoRepository.deleteById(id);
        log.info("Veículo deletado com sucesso. ID: {}", id);
    }
    
    /**
     * Busca um veículo por placa.
     * 
     * @param placa Placa do veículo
     * @return VeiculoDTO com os dados do veículo
     * @throws ResourceNotFoundException se o veículo não for encontrado
     */
    @Transactional(readOnly = true)
    public VeiculoDTO buscarVeiculoPorPlaca(String placa) {
        log.info("Buscando veículo por placa: {}", placa);
        
        Veiculo veiculo = veiculoRepository.findByPlaca(placa)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", "placa", placa));
        
        return converterParaDTO(veiculo);
    }
    
    /**
     * Valida se a placa é única no sistema.
     * 
     * @param placa Placa a ser validada
     * @throws BusinessException se a placa já estiver cadastrada
     */
    private void validarPlacaUnica(String placa) {
        if (veiculoRepository.existsByPlaca(placa)) {
            throw new BusinessException("Placa já cadastrada: " + placa);
        }
    }
    
    /**
     * Atualiza os dados de um veículo existente.
     * 
     * @param veiculo Entidade a ser atualizada
     * @param veiculoDTO Dados para atualização
     */
    private void atualizarDadosVeiculo(Veiculo veiculo, VeiculoDTO veiculoDTO) {
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setMarca(veiculoDTO.getMarca());
        veiculo.setQuilometragem(veiculoDTO.getQuilometragem());
        if (veiculoDTO.getStatus() != null) {
            veiculo.setStatus(veiculoDTO.getStatus());
        }
    }
    
    /**
     * Cria uma entidade Veiculo a partir de um DTO.
     * 
     * @param veiculoDTO DTO com os dados do veículo
     * @return Entidade Veiculo criada
     */
    private Veiculo criarEntidadeVeiculo(VeiculoDTO veiculoDTO) {
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setMarca(veiculoDTO.getMarca());
        veiculo.setQuilometragem(veiculoDTO.getQuilometragem());
        veiculo.setStatus(veiculoDTO.getStatus() != null ? veiculoDTO.getStatus() : com.desafio.model.StatusVeiculo.ATIVO);
        return veiculo;
    }
    
    /**
     * Converte uma entidade Veiculo para DTO.
     * 
     * @param veiculo Entidade a ser convertida
     * @return VeiculoDTO com os dados convertidos
     */
    private VeiculoDTO converterParaDTO(Veiculo veiculo) {
        return new VeiculoDTO(
            veiculo.getId(),
            veiculo.getPlaca(),
            veiculo.getMarca(),
            veiculo.getQuilometragem(),
            veiculo.getStatus(),
            veiculo.getCreatedAt(),
            veiculo.getUpdatedAt()
        );
    }
    
    /**
     * Converte uma entidade VeiculoPneu para DTO.
     * 
     * @param veiculoPneu Entidade a ser convertida
     * @return VeiculoPneuDTO com os dados convertidos
     */
    private VeiculoPneuDTO converterVeiculoPneuParaDTO(VeiculoPneu veiculoPneu) {
        return new VeiculoPneuDTO(
            veiculoPneu.getId(),
            veiculoPneu.getVeiculo().getId(),
            veiculoPneu.getPneu().getId(),
            veiculoPneu.getPosition(),
            veiculoPneu.getPneu().getNumeroFogo(),
            veiculoPneu.getPneu().getMarca(),
            veiculoPneu.getPneu().getPressaoAtual(),
            veiculoPneu.getCreatedAt(),
            veiculoPneu.getUpdatedAt()
        );
    }
} 