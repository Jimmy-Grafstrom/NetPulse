package se.jimmy.netpulse.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.jimmy.netpulse.config.NetPulseProperties;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogController.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean(answers = RETURNS_DEEP_STUBS)
    private NetPulseProperties properties;

    @Test
    void downloadLogs_WhenFileNotFound_Return404Exception() throws Exception {
        when(properties.logging().csvPath()).thenReturn("non-existing.csv");

        mockMvc.perform(get("/api/logs/download"))
                .andExpect(status().isNotFound());
    }
}