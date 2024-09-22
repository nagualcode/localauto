package br.nagualcode.carros.controller;

import br.nagualcode.carros.model.Carro;
import br.nagualcode.carros.repository.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarrosController.class)
public class CarrosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarroRepository carroRepository;

    private Carro carro;

    @BeforeEach
    public void setUp() {
        carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Fiat Uno");
        carro.setMarca("Fiat");
        carro.setPlaca("ABC-1234");
        carro.setCor("Branco");
    }

    @Test
    public void testGetCarroById() throws Exception {
        // Mocking the repository to return a Carro object when called
        Mockito.when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

        // Simulating a GET request to the controller
        mockMvc.perform(get("/api/carros/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.modelo").value("Fiat Uno"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.placa").value("ABC-1234"));
    }

    @Test
    public void testGetCarroById_NotFound() throws Exception {
        // Mocking the repository to return an empty result
        Mockito.when(carroRepository.findById(1L)).thenReturn(Optional.empty());

        // Simulating a GET request to the controller and expecting a 404 status
        mockMvc.perform(get("/api/carros/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCarro() throws Exception {
        // Mocking the repository save method
        Mockito.when(carroRepository.save(Mockito.any(Carro.class))).thenReturn(carro);

        String carroJson = "{\"modelo\":\"Fiat Uno\", \"marca\":\"Fiat\", \"placa\":\"ABC-1234\", \"cor\":\"Branco\"}";

        // Simulating a POST request to add a new Carro
        mockMvc.perform(post("/api/carros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carroJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.modelo").value("Fiat Uno"));
    }

    @Test
    public void testUpdateCarro() throws Exception {
        // Mocking the repository to return a Carro object
        Mockito.when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
        Mockito.when(carroRepository.save(Mockito.any(Carro.class))).thenReturn(carro);

        String updatedCarroJson = "{\"modelo\":\"Fiat Uno\", \"marca\":\"Fiat\", \"placa\":\"DEF-5678\", \"cor\":\"Preto\"}";

        // Simulating a PUT request to update an existing Carro
        mockMvc.perform(put("/api/carros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedCarroJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.placa").value("DEF-5678"));
    }

    @Test
    public void testDeleteCarro() throws Exception {
        // Simulating a DELETE request to remove a Carro
        mockMvc.perform(delete("/api/carros/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
