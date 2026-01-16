package se.jimmy.netpulse.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.jimmy.netpulse.model.AnalysisStatus;
import se.jimmy.netpulse.model.NetworkMetric;
import se.jimmy.netpulse.service.AnalysisService;
import se.jimmy.netpulse.service.FileLoggingService;
import se.jimmy.netpulse.service.ProbeService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoringScheduler {

    private final ProbeService probeService;
    private final AnalysisService analysisService;
    private final FileLoggingService loggingService;

    @Scheduled(fixedDelayString = "${netpulse.monitor.interval-ms}")
    public void runMonitoringCycle() {
        NetworkMetric metric = probeService.executeProbe();

        AnalysisStatus status = analysisService.analyze(metric);

        loggingService.logMetric(metric, status);

        log.info("Probe: {} | Latency: {}ms | Status: {}",
                metric.targetHost(), metric.latencyMs(), status);
    }
}
