package com.desafio.e2e;

import com.desafio.dto.PneuDTO;
import com.desafio.dto.VeiculoDTO;
import com.desafio.dto.VeiculoPneuDTO;
import com.desafio.model.StatusPneu;
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
class VeiculoPneuE2ETest {

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
    void testVincularPneuAoVeiculo() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("ABC1D23");
        veiculo.setMarca("Volvo");
        veiculo.setQuilometragem(150000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12345");
        pneu.setMarca("Michelin");
        pneu.setPressaoAtual(35.5);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // Vincular pneu ao veículo
        VeiculoPneuDTO veiculoPneu = new VeiculoPneuDTO();
        veiculoPneu.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu.setIdPneu(createdPneu.getId());
        veiculoPneu.setPosicao("A");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVeiculo").value(createdVeiculo.getId()))
                .andExpect(jsonPath("$.idPneu").value(createdPneu.getId()))
                .andExpect(jsonPath("$.posicao").value("A"));
    }

    @Test
    void testVincularPneuEmPosicaoOcupada() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("XYZ9W87");
        veiculo.setMarca("Mercedes-Benz");
        veiculo.setQuilometragem(89000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar dois pneus
        PneuDTO pneu1 = new PneuDTO();
        pneu1.setNumeroFogo("12346");
        pneu1.setMarca("Bridgestone");
        pneu1.setPressaoAtual(32.0);
        pneu1.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneu2 = new PneuDTO();
        pneu2.setNumeroFogo("12347");
        pneu2.setMarca("Goodyear");
        pneu2.setPressaoAtual(38.0);
        pneu2.setStatus(StatusPneu.DISPONIVEL);

        String pneu1Response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String pneu2Response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu1 = objectMapper.readValue(pneu1Response, PneuDTO.class);
        PneuDTO createdPneu2 = objectMapper.readValue(pneu2Response, PneuDTO.class);

        // Vincular primeiro pneu na posição A
        VeiculoPneuDTO veiculoPneu1 = new VeiculoPneuDTO();
        veiculoPneu1.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu1.setIdPneu(createdPneu1.getId());
        veiculoPneu1.setPosicao("A");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu1)))
                .andExpect(status().isCreated());

        // Tentar vincular segundo pneu na mesma posição A (deve falhar)
        VeiculoPneuDTO veiculoPneu2 = new VeiculoPneuDTO();
        veiculoPneu2.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu2.setIdPneu(createdPneu2.getId());
        veiculoPneu2.setPosicao("A");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDesvincularPneuDoVeiculo() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("DEF4G56");
        veiculo.setMarca("Scania");
        veiculo.setQuilometragem(120000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12348");
        pneu.setMarca("Pirelli");
        pneu.setPressaoAtual(32.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // Vincular pneu ao veículo
        VeiculoPneuDTO veiculoPneu = new VeiculoPneuDTO();
        veiculoPneu.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu.setIdPneu(createdPneu.getId());
        veiculoPneu.setPosicao("B");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu)))
                .andExpect(status().isCreated());

        // Desvincular pneu do veículo
        mockMvc.perform(delete("/api/veiculos-pneus/" + createdVeiculo.getId() + "/pneu/" + createdPneu.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testBuscarPneusDoVeiculo() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("HIJ7K89");
        veiculo.setMarca("Iveco");
        veiculo.setQuilometragem(75000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar dois pneus
        PneuDTO pneu1 = new PneuDTO();
        pneu1.setNumeroFogo("12349");
        pneu1.setMarca("Continental");
        pneu1.setPressaoAtual(36.5);
        pneu1.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneu2 = new PneuDTO();
        pneu2.setNumeroFogo("12350");
        pneu2.setMarca("Dunlop");
        pneu2.setPressaoAtual(34.0);
        pneu2.setStatus(StatusPneu.DISPONIVEL);

        String pneu1Response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String pneu2Response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu1 = objectMapper.readValue(pneu1Response, PneuDTO.class);
        PneuDTO createdPneu2 = objectMapper.readValue(pneu2Response, PneuDTO.class);

        // Vincular pneus ao veículo
        VeiculoPneuDTO veiculoPneu1 = new VeiculoPneuDTO();
        veiculoPneu1.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu1.setIdPneu(createdPneu1.getId());
        veiculoPneu1.setPosicao("A");

        VeiculoPneuDTO veiculoPneu2 = new VeiculoPneuDTO();
        veiculoPneu2.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu2.setIdPneu(createdPneu2.getId());
        veiculoPneu2.setPosicao("B");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu2)))
                .andExpect(status().isCreated());

        // Buscar pneus do veículo
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdVeiculo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testBuscarPneuNaPosicao() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("LMN0P12");
        veiculo.setMarca("Ford");
        veiculo.setQuilometragem(200000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12351");
        pneu.setMarca("Yokohama");
        pneu.setPressaoAtual(33.5);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // Vincular pneu ao veículo na posição C
        VeiculoPneuDTO veiculoPneu = new VeiculoPneuDTO();
        veiculoPneu.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu.setIdPneu(createdPneu.getId());
        veiculoPneu.setPosicao("C");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu)))
                .andExpect(status().isCreated());

        // Buscar pneu na posição C
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdVeiculo.getId() + "/posicao/C"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posicao").value("C"))
                .andExpect(jsonPath("$.idPneu").value(createdPneu.getId()));
    }

    @Test
    void testVerificarPosicaoOcupada() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("OPQ1R23");
        veiculo.setMarca("Toyota");
        veiculo.setQuilometragem(50000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12352");
        pneu.setMarca("Hankook");
        pneu.setPressaoAtual(37.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // Verificar posição D (deve estar livre)
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdVeiculo.getId() + "/posicao/D/ocupada"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        // Vincular pneu ao veículo na posição D
        VeiculoPneuDTO veiculoPneu = new VeiculoPneuDTO();
        veiculoPneu.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu.setIdPneu(createdPneu.getId());
        veiculoPneu.setPosicao("D");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu)))
                .andExpect(status().isCreated());

        // Verificar posição D (deve estar ocupada)
        mockMvc.perform(get("/api/veiculos-pneus/veiculo/" + createdVeiculo.getId() + "/posicao/D/ocupada"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testBuscarVeiculosDoPneu() throws Exception {
        // Criar dois veículos
        VeiculoDTO veiculo1 = new VeiculoDTO();
        veiculo1.setPlaca("STU4V56");
        veiculo1.setMarca("Volkswagen");
        veiculo1.setQuilometragem(80000);
        veiculo1.setStatus(StatusVeiculo.ATIVO);

        VeiculoDTO veiculo2 = new VeiculoDTO();
        veiculo2.setPlaca("WXY7Z89");
        veiculo2.setMarca("BMW");
        veiculo2.setQuilometragem(95000);
        veiculo2.setStatus(StatusVeiculo.ATIVO);

        String veiculo1Response = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String veiculo2Response = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo1 = objectMapper.readValue(veiculo1Response, VeiculoDTO.class);
        VeiculoDTO createdVeiculo2 = objectMapper.readValue(veiculo2Response, VeiculoDTO.class);

        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12353");
        pneu.setMarca("BF Goodrich");
        pneu.setPressaoAtual(35.5);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // Vincular pneu ao primeiro veículo
        VeiculoPneuDTO veiculoPneu1 = new VeiculoPneuDTO();
        veiculoPneu1.setIdVeiculo(createdVeiculo1.getId());
        veiculoPneu1.setIdPneu(createdPneu.getId());
        veiculoPneu1.setPosicao("A");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu1)))
                .andExpect(status().isCreated());

        // Buscar veículos onde o pneu está aplicado
        mockMvc.perform(get("/api/veiculos-pneus/pneu/" + createdPneu.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idVeiculo").value(createdVeiculo1.getId()));
    }

    @Test
    void testBuscarVeiculoComPneus() throws Exception {
        // Criar um veículo
        VeiculoDTO veiculo = new VeiculoDTO();
        veiculo.setPlaca("ABC1D24");
        veiculo.setMarca("Honda");
        veiculo.setQuilometragem(60000);
        veiculo.setStatus(StatusVeiculo.ATIVO);

        String veiculoResponse = mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VeiculoDTO createdVeiculo = objectMapper.readValue(veiculoResponse, VeiculoDTO.class);

        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12354");
        pneu.setMarca("Kumho");
        pneu.setPressaoAtual(25.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String pneuResponse = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(pneuResponse, PneuDTO.class);

        // Vincular pneu ao veículo
        VeiculoPneuDTO veiculoPneu = new VeiculoPneuDTO();
        veiculoPneu.setIdVeiculo(createdVeiculo.getId());
        veiculoPneu.setIdPneu(createdPneu.getId());
        veiculoPneu.setPosicao("A");

        mockMvc.perform(post("/api/veiculos-pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoPneu)))
                .andExpect(status().isCreated());

        // Buscar veículo com pneus
        mockMvc.perform(get("/api/veiculos/" + createdVeiculo.getId() + "/pneus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdVeiculo.getId()))
                .andExpect(jsonPath("$.pneusAplicados").isArray())
                .andExpect(jsonPath("$.pneusAplicados.length()").value(1))
                .andExpect(jsonPath("$.pneusAplicados[0].posicao").value("A"));
    }
} 