package se.jimmy.netpulse.repository;

import org.springframework.stereotype.Component;
import se.jimmy.netpulse.model.NetworkMetric;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MetricsRepository {

    private static final int MAX_HISTORY_SIZE = 100;

    private final Queue<NetworkMetric> history = new ConcurrentLinkedQueue<>();

    public void add(NetworkMetric metric) {
        history.add(metric);
        while (history.size() > MAX_HISTORY_SIZE) {
            history.poll();
        }
    }

    public List<NetworkMetric> getHistory() {
        return new ArrayList<>(history);
    }

    public NetworkMetric getLatest() {
        return history.stream().reduce((first, second) -> second).orElse(null);
    }
}
