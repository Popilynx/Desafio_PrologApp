package com.desafio.e2e;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class CenarioCompletoE2ETest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    void testCenarioCompleto() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 1. Criar veículos
        VeiculoDTO caminhao = new VeiculoDTO();
        caminhao.setPlaca("ABC1D23");
        caminhao.setMarca("Volvo");
        caminhao.setQuilometragem(150000);
        caminhao.setStatus(StatusVeiculo.ATIVO);

        String caminhaoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(caminhao)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdCaminhao = objectMapper.readValue(caminhaoResponse, VeiculoDTO.class);

        // 2. Criar pneus
        PneuDTO pneuA = new PneuDTO();
        pneuA.setNumeroFogo("188");
        pneuA.setMarca("Michelin");
        pneuA.setPressaoAtual(35.5);
        pneuA.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneuB = new PneuDTO();
        pneuB.setNumeroFogo("289");
        pneuB.setMarca("Bridgestone");
        pneuB.setPressaoAtual(32.0);
        pneuB.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneuC = new PneuDTO();
        pneuC.setNumeroFogo("178");
        pneuC.setMarca("Goodyear");
        pneuC.setPressaoAtual(38.0);
        pneuC.setStatus(StatusPneu.DISPONIVEL);

        String pneuAResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuA)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String pneuBResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuB)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String pneuCResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuC)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneuA = objectMapper.readValue(pneuAResponse, PneuDTO.class);
        PneuDTO createdPneuB = objectMapper.readValue(pneuBResponse, PneuDTO.class);
        PneuDTO createdPneuC = objectMapper.readValue(pneuCResponse, PneuDTO.class);

        // 3. Vincular pneus ao caminhão (exemplo do desafio)
        VeiculoPneuDTO aplicacaoA = new VeiculoPneuDTO();
        aplicacaoA.setIdVeiculo(createdCaminhao.getId());
        aplicacaoA.setIdPneu(createdPneuA.getId());
        aplicacaoA.setPosicao("A");

        VeiculoPneuDTO aplicacaoB = new VeiculoPneuDTO();
        aplicacaoB.setIdVeiculo(createdCaminhao.getId());
        aplicacaoB.setIdPneu(createdPneuB.getId());
        aplicacaoB.setPosicao("B");

        VeiculoPneuDTO aplicacaoC = new VeiculoPneuDTO();
        aplicacaoC.setIdVeiculo(createdCaminhao.getId());
        aplicacaoC.setIdPneu(createdPneuC.getId());
        aplicacaoC.setPosicao("C");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aplicacaoA)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aplicacaoB)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aplicacaoC)))
                .andExpect(status().isCreated());

        // 4. Verificar se o caminhão tem os pneus aplicados
        mockMvc.perform(get("/api/veiculos/" + createdCaminhao.getId() + "/pneus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCaminhao.getId()))
                .andExpect(jsonPath("$.placa").value("ABC1D23"))
                .andExpect(jsonPath("$.marca").value("Volvo"))
                .andExpect(jsonPath("$.pneusAplicados").isArray())
                .andExpect(jsonPath("$.pneusAplicados.length()").value(3));

        // 5. Verificar pneu específico na posição A
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdCaminhao.getId() + "/posicao/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posicao").value("A"))
                .andExpect(jsonPath("$.idPneu").value(createdPneuA.getId()));

        // 6. Verificar se posição D está livre
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdCaminhao.getId() + "/posicao/D/ocupada"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        // 7. Verificar se posição A está ocupada
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdCaminhao.getId() + "/posicao/A/ocupada"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 8. Listar todos os pneus do veículo
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdCaminhao.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        // 9. Verificar onde o pneu 188 está aplicado
        mockMvc.perform(get("/api/veiculos-pneus/pneu/" + createdPneuA.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idVeiculo").value(createdCaminhao.getId()));

        // 10. Atualizar pressão de um pneu
        mockMvc.perform(put("/api/pneus/" + createdPneuA.getId() + "/pressao")
                .param("novaPressao", "36.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pressaoAtual").value(36.0));

        // 11. Desvincular um pneu
        mockMvc.perform(delete("/api/veiculos-pneus/" + createdCaminhao.getId() + "/pneu/" + createdPneuC.getId()))
                .andExpect(status().isNoContent());

        // 12. Verificar se o pneu foi desvinculado
        mockMvc.perform(get("/api/veiculos/" + createdCaminhao.getId() + "/pneus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pneusAplicados.length()").value(2));

        // 13. Verificar se a posição C está livre
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdCaminhao.getId() + "/posicao/C/ocupada"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        // 14. Listar pneus disponíveis
        mockMvc.perform(get("/api/pneus/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 15. Buscar veículos por marca
        mockMvc.perform(get("/api/veiculos/marca/Volvo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].marca").value("Volvo"));

        // 16. Buscar pneus por marca
        mockMvc.perform(get("/api/pneus/marca/Michelin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].marca").value("Michelin"));

        // 17. Verificar health check
        mockMvc.perform(get("/api/test/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("API está funcionando!"));
    }
} 