package se.jimmy.netpulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.jimmy.netpulse.config.NetPulseProperties;
import se.jimmy.netpulse.model.AnalysisStatus;
import se.jimmy.netpulse.model.NetworkMetric;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final NetPulseProperties properties;

    public AnalysisStatus analyze(NetworkMetric metric) {
        if (!metric.isReachable()) {
            return AnalysisStatus.CRITICAL;
        }

        long latency = metric.latencyMs();
        long warningThreshold = properties.monitor().thresholds().warningsMs();
        long criticalThreshold = properties.monitor().thresholds().criticalMs();

        if (latency >= criticalThreshold) {
            log.error("CRITICAL: Latency {}ms exceeds threshold {}ms", latency, criticalThreshold);
            return AnalysisStatus.CRITICAL;
        }

        if (latency >= warningThreshold) {
            log.warn("WARNING: Latency {}ms exceeds threshold {}ms", latency, warningThreshold);
            return AnalysisStatus.WARNING;
        }

        return AnalysisStatus.OK;
    }
}
