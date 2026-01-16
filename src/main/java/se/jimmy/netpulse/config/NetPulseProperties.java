package se.jimmy.netpulse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "netpulse")
public record NetPulseProperties(
        Monitor monitor,
        Logging logging

) {
    public record Monitor (
            String targetHost,
            long intervalsMs,
            Thresholds thresholds
    ) {}

    public record Thresholds(
            long warningsMs,
            long criticalMs
    ) {}

    public record Logging(
            String csvPath
    ) {}
}
