package com.desafio.e2e;

import com.desafio.dto.PneuDTO;
import com.desafio.model.StatusPneu;
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
class PneuE2ETest {

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
    void testCriarPneu() throws Exception {
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12345");
        pneu.setMarca("Michelin");
        pneu.setPressaoAtual(35.5);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.numeroFogo").value("12345"))
                .andExpect(jsonPath("$.marca").value("Michelin"))
                .andExpect(jsonPath("$.pressaoAtual").value(35.5))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void testCriarPneuComPressaoNegativa() throws Exception {
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12346");
        pneu.setMarca("Bridgestone");
        pneu.setPressaoAtual(-10.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBuscarPneuPorId() throws Exception {
        // Primeiro criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12347");
        pneu.setMarca("Goodyear");
        pneu.setPressaoAtual(38.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(response, PneuDTO.class);

        // Agora buscar o pneu criado
        mockMvc.perform(get("/api/pneus/" + createdPneu.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdPneu.getId()))
                .andExpect(jsonPath("$.numeroFogo").value("12347"))
                .andExpect(jsonPath("$.marca").value("Goodyear"));
    }

    @Test
    void testBuscarPneuInexistente() throws Exception {
        mockMvc.perform(get("/api/pneus/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarTodosPneus() throws Exception {
        // Criar alguns pneus primeiro
        PneuDTO pneu1 = new PneuDTO();
        pneu1.setNumeroFogo("12348");
        pneu1.setMarca("Pirelli");
        pneu1.setPressaoAtual(32.0);
        pneu1.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneu2 = new PneuDTO();
        pneu2.setNumeroFogo("12349");
        pneu2.setMarca("Continental");
        pneu2.setPressaoAtual(36.5);
        pneu2.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu2)))
                .andExpect(status().isCreated());

        // Listar todos os pneus - deve conter pelo menos os 2 criados
        mockMvc.perform(get("/api/pneus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testAtualizarPneu() throws Exception {
        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12350");
        pneu.setMarca("Dunlop");
        pneu.setPressaoAtual(34.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(response, PneuDTO.class);

        // Atualizar o pneu
        PneuDTO pneuAtualizado = new PneuDTO();
        pneuAtualizado.setNumeroFogo("12350");
        pneuAtualizado.setMarca("Dunlop Sport");
        pneuAtualizado.setPressaoAtual(35.0);
        pneuAtualizado.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(put("/api/pneus/" + createdPneu.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Dunlop Sport"))
                .andExpect(jsonPath("$.pressaoAtual").value(35.0));
    }

    @Test
    void testDeletarPneu() throws Exception {
        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12351");
        pneu.setMarca("Yokohama");
        pneu.setPressaoAtual(33.5);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(response, PneuDTO.class);

        // Deletar o pneu
        mockMvc.perform(delete("/api/pneus/" + createdPneu.getId()))
                .andExpect(status().isNoContent());

        // Verificar se foi deletado
        mockMvc.perform(get("/api/pneus/" + createdPneu.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarPneusDisponiveis() throws Exception {
        // Criar pneus com status diferentes
        PneuDTO pneuDisponivel = new PneuDTO();
        pneuDisponivel.setNumeroFogo("12352");
        pneuDisponivel.setMarca("Hankook");
        pneuDisponivel.setPressaoAtual(37.0);
        pneuDisponivel.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneuEmUso = new PneuDTO();
        pneuEmUso.setNumeroFogo("12353");
        pneuEmUso.setMarca("BF Goodrich");
        pneuEmUso.setPressaoAtual(35.5);
        pneuEmUso.setStatus(StatusPneu.EM_USO);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuDisponivel)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuEmUso)))
                .andExpect(status().isCreated());

        // Buscar pneus disponíveis
        mockMvc.perform(get("/api/pneus/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("DISPONIVEL"));
    }

    @Test
    void testBuscarPneusComPressaoBaixa() throws Exception {
        // Criar pneus com pressões diferentes
        PneuDTO pneuPressaoBaixa = new PneuDTO();
        pneuPressaoBaixa.setNumeroFogo("12354");
        pneuPressaoBaixa.setMarca("Kumho");
        pneuPressaoBaixa.setPressaoAtual(25.0);
        pneuPressaoBaixa.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneuPressaoNormal = new PneuDTO();
        pneuPressaoNormal.setNumeroFogo("12355");
        pneuPressaoNormal.setMarca("Toyo");
        pneuPressaoNormal.setPressaoAtual(35.0);
        pneuPressaoNormal.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuPressaoBaixa)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuPressaoNormal)))
                .andExpect(status().isCreated());

        // Buscar pneus com pressão baixa (< 30 PSI)
        mockMvc.perform(get("/api/pneus/pressao-baixa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].pressaoAtual").value(25.0));
    }

    @Test
    void testBuscarPneusPorMarca() throws Exception {
        // Criar pneus da mesma marca
        PneuDTO pneu1 = new PneuDTO();
        pneu1.setNumeroFogo("12356");
        pneu1.setMarca("Michelin");
        pneu1.setPressaoAtual(36.0);
        pneu1.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneu2 = new PneuDTO();
        pneu2.setNumeroFogo("12357");
        pneu2.setMarca("Michelin");
        pneu2.setPressaoAtual(38.0);
        pneu2.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu2)))
                .andExpect(status().isCreated());

        // Buscar por marca
        mockMvc.perform(get("/api/pneus/marca/Michelin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].marca").value("Michelin"))
                .andExpect(jsonPath("$[1].marca").value("Michelin"));
    }

    @Test
    void testBuscarPneusPorStatus() throws Exception {
        // Criar pneus com status diferentes
        PneuDTO pneuDisponivel = new PneuDTO();
        pneuDisponivel.setNumeroFogo("12358");
        pneuDisponivel.setMarca("Bridgestone");
        pneuDisponivel.setPressaoAtual(34.5);
        pneuDisponivel.setStatus(StatusPneu.DISPONIVEL);

        PneuDTO pneuEmUso = new PneuDTO();
        pneuEmUso.setNumeroFogo("12359");
        pneuEmUso.setMarca("Goodyear");
        pneuEmUso.setPressaoAtual(36.0);
        pneuEmUso.setStatus(StatusPneu.EM_USO);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuDisponivel)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneuEmUso)))
                .andExpect(status().isCreated());

        // Buscar por status DISPONIVEL
        mockMvc.perform(get("/api/pneus/status/DISPONIVEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("DISPONIVEL"));
    }

    @Test
    void testBuscarPneuPorNumeroFogo() throws Exception {
        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12360");
        pneu.setMarca("Pirelli");
        pneu.setPressaoAtual(37.5);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated());

        // Buscar por número de fogo
        mockMvc.perform(get("/api/pneus/numero-fogo/12360"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroFogo").value("12360"))
                .andExpect(jsonPath("$.marca").value("Pirelli"));
    }

    @Test
    void testAtualizarPressaoPneu() throws Exception {
        // Criar um pneu
        PneuDTO pneu = new PneuDTO();
        pneu.setNumeroFogo("12361");
        pneu.setMarca("Continental");
        pneu.setPressaoAtual(32.0);
        pneu.setStatus(StatusPneu.DISPONIVEL);

        String response = mockMvc.perform(post("/api/pneus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pneu)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PneuDTO createdPneu = objectMapper.readValue(response, PneuDTO.class);

        // Atualizar pressão
        mockMvc.perform(put("/api/pneus/" + createdPneu.getId() + "/pressao")
                .param("novaPressao", "35.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pressaoAtual").value(35.0));
    }
} 