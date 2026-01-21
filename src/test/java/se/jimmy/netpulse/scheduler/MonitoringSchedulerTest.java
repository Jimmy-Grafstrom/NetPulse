package se.jimmy.netpulse.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.jimmy.netpulse.model.AnalysisStatus;
import se.jimmy.netpulse.model.NetworkMetric;
import se.jimmy.netpulse.repository.MetricsRepository;
import se.jimmy.netpulse.service.AnalysisService;
import se.jimmy.netpulse.service.FileLoggingService;
import se.jimmy.netpulse.service.ProbeService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoringSchedulerTest {


    @Mock
    private ProbeService probeService;
    @Mock
    private AnalysisService analysisService;
    @Mock
    private FileLoggingService fileLoggingService;
    @Mock
    private MetricsRepository metricsRepository;

    @InjectMocks
    private MonitoringScheduler monitoringScheduler;

    @Test
    void runMonitoringCycle_ShouldTriggerServicesCorrectly() {
        NetworkMetric mockMetric = new NetworkMetric(LocalDateTime.now(), "google.com", 25, true);
        AnalysisStatus mockStatus = AnalysisStatus.OK;

        when(probeService.executeProbe()).thenReturn(mockMetric);
        when(analysisService.analyze(mockMetric)).thenReturn(mockStatus);

        monitoringScheduler.runMonitoringCycle();

        verify(probeService).executeProbe();
        verify(analysisService).analyze(mockMetric);
        verify(fileLoggingService).logMetric(mockMetric, mockStatus);
        verify(metricsRepository).add(mockMetric);
    }
}