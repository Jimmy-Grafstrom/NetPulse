package se.jimmy.netpulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.jimmy.netpulse.config.NetPulseProperties;
import se.jimmy.netpulse.model.NetworkMetric;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProbeService {
    private final NetPulseProperties properties;

    public NetworkMetric executeProbe() {
        String host = properties.monitor().targetHost();
        int timeoutMs = (int) properties.monitor().thresholds().criticalMs();

        Instant start = Instant.now();
        boolean reachable = false;

        try {
            reachable = InetAddress.getByName(host).isReachable(timeoutMs);
        } catch (IOException e) {
            log.warn("Failed to reach {}: {}", host, e.getMessage());
        }

        Instant end = Instant.now();
        long latency = Duration.between(start, end).toMillis();

        return new NetworkMetric(
                LocalDateTime.now(),
                host,
                latency,
                reachable
        );
    }
}
