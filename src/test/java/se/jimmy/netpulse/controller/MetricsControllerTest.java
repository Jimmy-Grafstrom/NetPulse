package se.jimmy.netpulse.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.jimmy.netpulse.model.NetworkMetric;
import se.jimmy.netpulse.repository.MetricsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetricsController.class)
class MetricsControllerTest {
    private NetworkMetric metric;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetricsRepository repository;

    @BeforeEach
    void setup() {
        metric = new NetworkMetric(LocalDateTime.now(), "google.com", 15, true);

    }

    @Test
    void getLatest_ShouldReturnMetric() throws Exception {
        when(repository.getLatest()).thenReturn(metric);

        mockMvc.perform(get("/api/metrics/latest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetHost").value("google.com"))
                .andExpect(jsonPath("$.latencyMs").value(15));
    }

    @Test
    void getHistory_ShouldReturnList() throws Exception {
        when(repository.getHistory()).thenReturn(List.of(metric));

        mockMvc.perform(get("/api/metrics/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].targetHost").value("google.com"));

    }
}