package se.jimmy.netpulse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.jimmy.netpulse.config.NetPulseProperties;
import se.jimmy.netpulse.model.AnalysisStatus;
import se.jimmy.netpulse.model.NetworkMetric;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisServiceTest {

    private NetworkMetric metric;
    private AnalysisService service;
    private AnalysisStatus status;
    private LocalDateTime now;
    private Long latency;
    private String host;

    @BeforeEach
    void setup() {
        now = LocalDateTime.now();
        host = "testHost";
        var thresholds = new NetPulseProperties.Thresholds(100L, 500L);
        var monitor = new NetPulseProperties.Monitor(host , 5000L, thresholds);
        var logging = new NetPulseProperties.Logging("testPath/path.csv");
        NetPulseProperties properties = new NetPulseProperties(monitor, logging);
        service = new AnalysisService(properties);
    }

    @Test
    void analyze_WhenNetworkMetricIsNotReachable_ShouldReturnCritical() {
        latency = 1000L;
        metric = new NetworkMetric(now, host, latency, false);

        status = service.analyze(metric);

        assertEquals(AnalysisStatus.CRITICAL, status);
    }

    @Test
    void  analyze_WhenLatencyIsBellow100_ShouldReturnOk() {
        latency = 99L;
        metric = new NetworkMetric(now, host, latency, true);

        status = service.analyze(metric);

        assertEquals(AnalysisStatus.OK, status);
    }

    @Test
    void analyze_WhenLatencyIs100_ShouldReturnWarning() {
        latency = 100L;
        metric = new NetworkMetric(now, host, latency, true);

        status = service.analyze(metric);

        assertEquals(AnalysisStatus.WARNING, status);
    }

    @Test
    void analyze_WhenLatencyIs499_ShouldReturnWarning() {
        latency = 499L;
        metric = new NetworkMetric(now, host, latency, true);

        status = service.analyze(metric);

        assertEquals(AnalysisStatus.WARNING, status);
    }

    @Test
    void analyze_WhenLatencyIsOver500_ShouldReturnCritical() {
        latency = 500L;
        metric = new NetworkMetric(now, host, latency, true);

        status = service.analyze(metric);

        assertEquals(AnalysisStatus.CRITICAL, status);
    }

}