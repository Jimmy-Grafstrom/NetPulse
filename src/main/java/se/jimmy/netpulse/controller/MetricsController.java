package se.jimmy.netpulse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jimmy.netpulse.model.NetworkMetric;
import se.jimmy.netpulse.repository.MetricsRepository;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsRepository repository;

    @GetMapping("/latest")
    public NetworkMetric getLatest() {
        return repository.getLatest();
    }

    @GetMapping("/history")
    public List<NetworkMetric> getHistory() {
        return repository.getHistory();
    }
}
