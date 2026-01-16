package se.jimmy.netpulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.jimmy.netpulse.config.NetPulseProperties;
import se.jimmy.netpulse.model.AnalysisStatus;
import se.jimmy.netpulse.model.NetworkMetric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileLoggingService {

    private final NetPulseProperties properties;
    private static final String CSV_HEADER = "Timestamp,Target,LatencyMs,Status,Reachable";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logMetric(NetworkMetric metric, AnalysisStatus status) {
        String filePath = properties.logging().csvPath();
        Path path = Paths.get(filePath);

        try {
            ensureFilePathExists(path);
            String csvLine = formatCsvLine(metric, status);
            Files.writeString(path, csvLine + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Failed to write to CSV log: {}", e.getMessage());
        }
    }

    private void ensureFilePathExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, CSV_HEADER + System.lineSeparator(), StandardOpenOption.CREATE);
            log.info("Created new log file at {}", path);
        }
    }

    private String formatCsvLine(NetworkMetric metric, AnalysisStatus status) {
        return String.join(",",
                metric.timestamp().format(DATE_FORMATTER),
                metric.targetHost(),
                String.valueOf(metric.latencyMs()),
                status.name(),
                String.valueOf(metric.isReachable())
        );
    }
}
