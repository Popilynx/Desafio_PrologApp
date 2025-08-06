package com.desafio.e2e;

import com.desafio.dto.VeiculoDTO;
import com.desafio.model.StatusVeiculo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
class VeiculoE2ETest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testCriarVeiculo() throws Exception {
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("ABC1D23");
        veiculo.setMarca("Volvo");
        veiculo.setQuilometragem(150000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.placa").value("ABC1D23"))
                .andExpect(jsonPath("$.marca").value("Volvo"))
                .andExpect(jsonPath("$.quilometragem").value(150000))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    void testCriarVeiculoComPlacaInvalida() throws Exception {
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("INVALID");
        veiculo.setMarca("Volvo");
        veiculo.setQuilometragem(150000);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBuscarVeiculoPorId() throws Exception {
        // Primeiro criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("XYZ9W87");
        veiculo.setMarca("Mercedes-Benz");
        veiculo.setQuilometragem(89000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String response = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(response, VeiculoDTO.class);

        // Agora buscar o veículo criado
        mockMvc.perform(get("/api/veiculos/" + createdVeiculo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdVeiculo.getId()))
                .andExpect(jsonPath("$.placa").value("XYZ9W87"))
                .andExpect(jsonPath("$.marca").value("Mercedes-Benz"));
    }

    @Test
    void testBuscarVeiculoInexistente() throws Exception {
        mockMvc.perform(get("/api/veiculos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarTodosVeiculos() throws Exception {
        // Criar alguns veículos primeiro
        VeiculoDTO veiculo1 = new VeiculoDTO();
        veiculo1.setPlaca("DEF4G56");
        veiculo1.setMarca("Scania");
        veiculo1.setQuilometragem(120000);
        veiculo1.setStatus(StatusVeiculo.ATIVO);

        VeiculoDTO veiculo2 = new VeiculoDTO();
        veiculo2.setPlaca("HIJ7K89");
        veiculo2.setMarca("Iveco");
        veiculo2.setQuilometragem(75000);
        veiculo2.setStatus(StatusVeiculo.INATIVO);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo2)))
                .andExpect(status().isCreated());

        // Listar todos os veículos - deve retornar apenas os 2 criados neste teste
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testAtualizarVeiculo() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("LMN0P12");
        veiculo.setMarca("Ford");
        veiculo.setQuilometragem(200000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String response = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(response, VeiculoDTO.class);

        // Atualizar o veículo
        VeiculoDTO veiculoAtualizado = new VeiculoDTO();
        veiculoAtualizado.setPlaca("LMN0P12");
        veiculoAtualizado.setMarca("Ford Focus");
        veiculoAtualizado.setQuilometragem(210000);
        veiculoAtualizado.setStatus(StatusVeiculo.ATIVO);

        mockMvc.perform(put("/api/veiculos/" + createdVeiculo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Ford Focus"))
                .andExpect(jsonPath("$.quilometragem").value(210000));
    }

    @Test
    void testDeletarVeiculo() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("OPQ1R23");
        veiculo.setMarca("Toyota");
        veiculo.setQuilometragem(50000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String response = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(response, VeiculoDTO.class);

        // Deletar o veículo
        mockMvc.perform(delete("/api/veiculos/" + createdVeiculo.getId()))
                .andExpect(status().isNoContent());

        // Verificar se foi deletado
        mockMvc.perform(get("/api/veiculos/" + createdVeiculo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarVeiculosPorMarca() throws Exception {
        // Criar veículos da mesma marca
        VeiculoDTO veiculo1 = new VeiculoDTO();
        veiculo1.setPlaca("STU4V56");
        veiculo1.setMarca("Volkswagen");
        veiculo1.setQuilometragem(80000);
        veiculo1.setStatus(StatusVeiculo.ATIVO);

        VeiculoDTO veiculo2 = new VeiculoDTO();
        veiculo2.setPlaca("WXY7Z89");
        veiculo2.setMarca("Volkswagen");
        veiculo2.setQuilometragem(95000);
        veiculo2.setStatus(StatusVeiculo.ATIVO);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo2)))
                .andExpect(status().isCreated());

        // Buscar por marca
        mockMvc.perform(get("/api/veiculos/marca/Volkswagen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].marca").value("Volkswagen"))
                .andExpect(jsonPath("$[1].marca").value("Volkswagen"));
    }

    @Test
    void testBuscarVeiculosPorStatus() throws Exception {
        // Criar veículos com status diferentes
        VeiculoDTO veiculoAtivo = new VeiculoDTO();
        veiculoAtivo.setPlaca("ABC1D24");
        veiculoAtivo.setMarca("Honda");
        veiculoAtivo.setQuilometragem(60000);
        veiculoAtivo.setStatus(StatusVeiculo.ATIVO);

        VeiculoDTO veiculoInativo = new VeiculoDTO();
        veiculoInativo.setPlaca("XYZ9W88");
        veiculoInativo.setMarca("Hyundai");
        veiculoInativo.setQuilometragem(180000);
        veiculoInativo.setStatus(StatusVeiculo.INATIVO);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoAtivo)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoInativo)))
                .andExpect(status().isCreated());

        // Buscar por status ATIVO
        mockMvc.perform(get("/api/veiculos/status/ATIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("ATIVO"));
    }

    @Test
    void testBuscarVeiculoPorPlaca() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("DEF4G57");
        veiculo.setMarca("Nissan");
        veiculo.setQuilometragem(70000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated());

        // Buscar por placa
        mockMvc.perform(get("/api/veiculos/placa/DEF4G57"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("DEF4G57"))
                .andExpect(jsonPath("$.marca").value("Nissan"));
    }
} 