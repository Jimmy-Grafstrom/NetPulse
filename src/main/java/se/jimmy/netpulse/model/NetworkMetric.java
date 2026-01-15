package se.jimmy.netpulse.model;

import java.time.LocalDateTime;

public record NetworkMetric(
        LocalDateTime timestamp,
        String targetHost,
        long latencyMs,
        boolean isReachable
) {
}
