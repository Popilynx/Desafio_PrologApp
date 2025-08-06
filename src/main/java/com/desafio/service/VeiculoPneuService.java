package com.desafio.service;

import com.desafio.dto.VeiculoPneuDTO;
import com.desafio.model.Pneu;
import com.desafio.model.StatusPneu;
import com.desafio.model.Veiculo;
import com.desafio.model.VeiculoPneu;
import com.desafio.repository.PneuRepository;
import com.desafio.repository.VeiculoRepository;
import com.desafio.repository.VeiculoPneuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeiculoPneuService {
    
    @Autowired
    private VeiculoPneuRepository veiculoPneuRepository;
    
    @Autowired
    private VeiculoRepository veiculoRepository;
    
    @Autowired
    private PneuRepository pneuRepository;
    
    // Vincular pneu a um veículo
    public VeiculoPneuDTO vincularPneuAoVeiculo(VeiculoPneuDTO veiculoPneuDTO) {
        // Verificar se veículo existe
        Optional<Veiculo> veiculoOpt = veiculoRepository.findById(veiculoPneuDTO.getIdVeiculo());
        if (veiculoOpt.isEmpty()) {
            throw new RuntimeException("Veículo não encontrado com ID: " + veiculoPneuDTO.getIdVeiculo());
        }
        
        // Verificar se pneu existe
        Optional<Pneu> pneuOpt = pneuRepository.findById(veiculoPneuDTO.getIdPneu());
        if (pneuOpt.isEmpty()) {
            throw new RuntimeException("Pneu não encontrado com ID: " + veiculoPneuDTO.getIdPneu());
        }
        
        Veiculo veiculo = veiculoOpt.get();
        Pneu pneu = pneuOpt.get();
        
        // Verificar se pneu está disponível
        if (pneu.getStatus() != StatusPneu.DISPONIVEL) {
            throw new RuntimeException("Pneu não está disponível para aplicação");
        }
        
        // Verificar se posição já está ocupada no veículo
        if (veiculoPneuRepository.existsByVeiculoIdAndPosition(veiculo.getId(), veiculoPneuDTO.getPosicao())) {
            throw new RuntimeException("Posição " + veiculoPneuDTO.getPosicao() + " já está ocupada no veículo");
        }
        
        // Verificar se pneu já está aplicado em outro veículo
        if (veiculoPneuRepository.existsByPneuId(pneu.getId())) {
            throw new RuntimeException("Pneu já está aplicado em outro veículo");
        }
        
        // Criar relacionamento
        VeiculoPneu veiculoPneu = new VeiculoPneu();
        veiculoPneu.setVeiculo(veiculo);
        veiculoPneu.setPneu(pneu);
        veiculoPneu.setPosition(veiculoPneuDTO.getPosicao());
        
        // Salvar no banco
        VeiculoPneu savedVeiculoPneu = veiculoPneuRepository.save(veiculoPneu);
        
        // Atualizar status do pneu para "Em Uso"
        pneu.setStatus(StatusPneu.EM_USO);
        pneuRepository.save(pneu);
        
        // Retornar DTO
        return converterParaDTO(savedVeiculoPneu);
    }
    
    // Desvincular pneu de um veículo
    public void desvincularPneuDoVeiculo(Long idVeiculo, Long idPneu) {
        // Verificar se aplicação existe
        Optional<VeiculoPneu> veiculoPneuOpt = veiculoPneuRepository.findByVeiculoIdAndPneuId(idVeiculo, idPneu);
        if (veiculoPneuOpt.isEmpty()) {
            throw new RuntimeException("Pneu não está aplicado neste veículo");
        }
        
        VeiculoPneu veiculoPneu = veiculoPneuOpt.get();
        
        // Buscar o pneu para atualizar status
        Optional<Pneu> pneuOpt = pneuRepository.findById(idPneu);
        if (pneuOpt.isPresent()) {
            Pneu pneu = pneuOpt.get();
            pneu.setStatus(StatusPneu.DISPONIVEL);
            pneuRepository.save(pneu);
        }
        
        // Deletar relacionamento
        veiculoPneuRepository.delete(veiculoPneu);
    }
    
    // Buscar todos os pneus de um veículo
    public List<VeiculoPneuDTO> buscarPneusDoVeiculo(Long idVeiculo) {
        List<VeiculoPneu> veiculoPneus = veiculoPneuRepository.findByVeiculoId(idVeiculo);
        return veiculoPneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar pneu em uma posição específica
    public VeiculoPneuDTO buscarPneuNaPosicao(Long idVeiculo, String posicao) {
        Optional<VeiculoPneu> veiculoPneu = veiculoPneuRepository.findByVeiculoIdAndPosition(idVeiculo, posicao);
        if (veiculoPneu.isPresent()) {
            return converterParaDTO(veiculoPneu.get());
        }
        throw new RuntimeException("Nenhum pneu encontrado na posição " + posicao + " do veículo");
    }
    
    // Verificar se uma posição está ocupada
    public boolean verificarPosicaoOcupada(Long idVeiculo, String posicao) {
        return veiculoPneuRepository.existsByVeiculoIdAndPosition(idVeiculo, posicao);
    }
    
    // Buscar veículos onde um pneu está aplicado
    public List<VeiculoPneuDTO> buscarVeiculosDoPneu(Long idPneu) {
        List<VeiculoPneu> veiculoPneus = veiculoPneuRepository.findByPneuId(idPneu);
        return veiculoPneus.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Método auxiliar para converter VeiculoPneu para VeiculoPneuDTO
    private VeiculoPneuDTO converterParaDTO(VeiculoPneu veiculoPneu) {
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