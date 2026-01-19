package se.jimmy.netpulse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import se.jimmy.netpulse.config.NetPulseProperties;
import se.jimmy.netpulse.model.AnalysisStatus;
import se.jimmy.netpulse.model.NetworkMetric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLoggingServiceTest {

    @TempDir
    private Path tempDir;
    private Path logFile;
    private String csvPath;
    private NetPulseProperties properties;
    private NetworkMetric metric;
    private FileLoggingService service;

    @BeforeEach
    void setUp() {
        logFile = tempDir.resolve("test.csv");
        csvPath = logFile.toString();

        var thresholds = new NetPulseProperties.Thresholds(100L, 500L);
        var logging = new NetPulseProperties.Logging(csvPath);
        var monitor = new NetPulseProperties.Monitor("testHost", 5000L, thresholds);
        properties = new NetPulseProperties(monitor, logging);

        metric = new NetworkMetric(LocalDateTime.now(), "testHost", 50L,true);
        service = new FileLoggingService(properties);
    }

    @Test
    void logMetric_ShouldCreateCsvFileWithHeaderOnStartup() throws IOException {
        service.init();

        List<String> lines = Files.readAllLines(logFile);
        assertTrue(Files.exists(Path.of(csvPath)));
        assertEquals(1, lines.size(), "Csv file should only contain header at startup");
        assertTrue(lines.getFirst().contains("Timestamp,Target,LatencyMs,Status,Reachable"));
    }
    @Test
    void logMetric_ShouldCreateCsvFileAndWriteContent() throws IOException {
        service.init();

        service.logMetric(metric, AnalysisStatus.OK);
        List<String> lines = Files.readAllLines(logFile);

        assertTrue(Files.exists(logFile));
        assertEquals(2, lines.size(), "Should contain header and one row of data");
        assertTrue(lines.getFirst().contains("Timestamp,Target,LatencyMs,Status,Reachable"));
        assertTrue(lines.get(1).contains("testHost"));
        assertTrue(lines.get(1).contains("OK"));
    }
}