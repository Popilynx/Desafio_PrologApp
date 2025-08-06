package com.desafio;

import com.desafio.dto.PneuDTO;
import com.desafio.dto.VeiculoDTO;
import com.desafio.dto.VeiculoPneuDTO;
import com.desafio.model.StatusPneu;
import com.desafio.model.StatusVeiculo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class TesteIntegracao {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCenarioCompleto() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 1. Testar health check
        mockMvc.perform(get("/api/test/health"))
                .andExpect(status().isOk());

        // 2. Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("TES1T23");
        veiculo.setMarca("Teste");
        veiculo.setQuilometragem(100000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // 3. Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("TEST456");
        pneu.setMarca("Teste");
        pneu.setPressaoAtual(35.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // 4. Vincular pneu ao veículo
        VeiculoPneuDTO veiculoPneu = new VeiculoPneuDTO();
        veiculoPneu.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu.setIdPneu(createdPneu.getId());
        veiculoPneu.setPosicao("A");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu)))
                .andExpect(status().isCreated());

        // 5. Verificar se o veículo tem o pneu aplicado
        mockMvc.perform(get("/api/veiculos/" + createdVeiculo.getId() + "/pneus"))
                .andExpect(status().isOk());

        // 6. Listar todos os veículos
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk());

        // 7. Listar todos os pneus
        mockMvc.perform(get("/api/pneus"))
                .andExpect(status().isOk());

        // 8. Desvincular pneu do veículo
        mockMvc.perform(delete("/api/veiculos-pneus/" + createdVeiculo.getId() + "/pneu/" + createdPneu.getId()))
                .andExpect(status().isNoContent());

        // 9. Deletar o pneu
        mockMvc.perform(delete("/api/pneus/" + createdPneu.getId()))
                .andExpect(status().isNoContent());

        // 10. Deletar o veículo
        mockMvc.perform(delete("/api/veiculos/" + createdVeiculo.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testValidacoes() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Testar validação de placa inválida
        VeiculoDTO veiculoInvalido = new VeiculoDTO();
        veiculoInvalido.setPlaca("INVALID");
        veiculoInvalido.setMarca("Teste");
        veiculoInvalido.setQuilometragem(100000);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoInvalido)))
                .andExpect(status().isBadRequest());

        // Testar validação de pressão negativa
        PneuDTO pneuInvalido = new PneuDTO();
        pneuInvalido.setNumeroFogo("TEST789");
        pneuInvalido.setMarca("Teste");
        pneuInvalido.setPressaoAtual(-10.0);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuInvalido)))
                .andExpect(status().isBadRequest());
    }


} 